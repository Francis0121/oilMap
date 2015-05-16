package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-15.
 */

import java.io.*;
import java.util.*;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.location.Location;
import android.os.*;
import android.text.format.Time;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.oilMap.client.MainActivity;
import com.oilMap.client.R;
import com.oilMap.client.gps.GpsInfo;
import com.oilMap.client.info.MainPage;
import com.oilMap.client.info.NavigationActivity;
import com.oilMap.client.info.OilInfoActivity;
import com.oilMap.client.util.BackPressCloseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class Bluetooth_reception extends Activity implements AdapterView.OnItemClickListener {

    static final int ACTION_ENABLE_BT = 101;
    public DataParsing i = new DataParsing(); ////////////////////////////

    TextView mTextMsg;
    BluetoothAdapter mBA;
    ListView mListDevice;
    ArrayList<String> mArDevice; // 원격 디바이스 목록

    static final String  BLUE_NAME = "BluetoothEx";  // 접속시 사용하는 이름

    // 접속시 사용하는 고유 ID
    static final UUID BLUE_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    ClientThread mCThread = null; // 클라이언트 소켓 접속 스레드
    ServerThread mSThread = null; // 서버 소켓 접속 스레드
    SocketThread mSocketThread = null; // 데이터 송수신 스레드

    //급가속 시 서버로 보냄/////
    public double km_per_liter=0;
    public double first_dis=0,first_fuel=0;
    public double fuel_capacity=0.0; // 현제 연료량
    ////지우기
    public double rpm_last=0.0, rpm_now=0.0, rpm_sub=999.0;
    public long time_last=0;
    public Date d=new Date();
    ///////////////////////
    //////

    GpsInfo gps = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.gps_main);


        mTextMsg = (TextView)findViewById(R.id.textMessage);
        mBA = BluetoothAdapter.getDefaultAdapter();

        // ListView 초기화
        initListView();

        //Thread GpsThread=new GpsThread();
        //GpsThread.start();

        // 블루투스 사용 가능상태 판단
        boolean isBlue = canUseBluetooth();
        if( isBlue )
            // 페어링된 원격 디바이스 목록 구하기
            getParedDevice();
        ////
        gps = new GpsInfo(Bluetooth_reception.this);
        // while(true){
        gpsUsing();
        // SystemClock.sleep(1000);
        // }
    }

    public void gpsUsing(){
        if (gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            showMessage(String.format("%.3f",latitude)+"/"+String.format("%.3f",longitude));
        }
    }



    // 블루투스 사용 가능상태 판단
    public boolean canUseBluetooth() {

        // 블루투스 어댑터를 구한다
        mBA = BluetoothAdapter.getDefaultAdapter();

        // 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
        if( mBA == null ) {
            mTextMsg.setText("Device not found");
            return false;
        }

        mTextMsg.setText("Device is exist");

        // 블루투스 활성화 상태라면 함수 탈출
        if( mBA.isEnabled() ) {
            mTextMsg.append("\nDevice can use");
            return true;
        }

        // 사용자에게 블루투스 활성화를 요청한다
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, ACTION_ENABLE_BT);
        return false;
    }

    // 블루투스 활성화 요청 결과 수신
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == ACTION_ENABLE_BT ) {

            // 사용자가 블루투스 활성화 승인했을때
            if( resultCode == RESULT_OK ) {
                mTextMsg.append("\nDevice can use");

                // 페어링된 원격 디바이스 목록 구하기
                getParedDevice();
            }
            // 사용자가 블루투스 활성화 취소했을때
            else {
                mTextMsg.append("\nDevice can not use");
            }
        }
    }

    // 원격 디바이스 검색 시작
    public void startFindDevice() {

        // 원격 디바이스 검색 중지
        stopFindDevice();

        showMessage("Bluetooth 장치 OBD를 선택해주세요.");

        // 디바이스 검색 시작
        mBA.startDiscovery();

        // 원격 디바이스 검색 이벤트 리시버 등록
        registerReceiver(mBlueRecv, new IntentFilter( BluetoothDevice.ACTION_FOUND ));
    }

    // 디바이스 검색 중지
    public void stopFindDevice() {

        // 현재 디바이스 검색 중이라면 취소한다
        if( mBA.isDiscovering() ) {
            mBA.cancelDiscovery();

            // 브로드캐스트 리시버를 등록 해제한다
            unregisterReceiver(mBlueRecv);
        }
    }

    // 원격 디바이스 검색 이벤트 수신
    BroadcastReceiver mBlueRecv = new BroadcastReceiver() {

        public void  onReceive(Context context, Intent intent) {

            if( intent.getAction() == BluetoothDevice.ACTION_FOUND ) {

                // 인텐트에서 디바이스 정보 추출
                BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );

                if( device.getBondState() != BluetoothDevice.BOND_BONDED ) // 페어링된 디바이스가 아니라면
                    // 디바이스를 목록에 추가
                    addDeviceToList(device.getName(), device.getAddress());
            }
        }
    };

    // 디바이스를 ListView 에 추가
    public void addDeviceToList(String name, String address) {

        // ListView 와 연결된 ArrayList 에 새로운 항목을 추가
        String deviceInfo = name + " - " + address;
        Log.d("tag1", "Device Find: " + deviceInfo);

        mArDevice.add(deviceInfo);

        // 화면을 갱신한다
        ArrayAdapter adapter = (ArrayAdapter)mListDevice.getAdapter();
        adapter.notifyDataSetChanged();
    }

    // ListView 초기화
    public void initListView() {

        // 어댑터 생성
        mArDevice = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArDevice);

        // ListView 에 어댑터와 이벤트 리스너를 지정
        mListDevice = (ListView)findViewById(R.id.listDevice);
        mListDevice.setAdapter(adapter);
        mListDevice.setOnItemClickListener(this);
    }

    // 페어링된 원격 디바이스 목록 구하기
    public void getParedDevice() {

        if( mSThread != null ) return;

        // 서버 소켓 접속을 위한 스레드 생성 & 시작
        mSThread = new ServerThread();
        mSThread.start();

        // 블루투스 어댑터에서 페어링된 원격 디바이스 목록을 구한다
        Set<BluetoothDevice> devices = mBA.getBondedDevices();

        // 디바이스 목록에서 하나씩 추출
        for( BluetoothDevice device : devices ) {
            addDeviceToList(device.getName(), device.getAddress()); // 디바이스를 목록에 추가
        }

        // 원격 디바이스 검색 시작
        startFindDevice();

        // 다른 디바이스에 자신을 노출
        // setDiscoverable();
    }

    // ListView 항목 선택 이벤트 함수
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        // 사용자가 선택한 항목의 내용을 구한다
        String strItem = mArDevice.get(position);

        // 사용자가 선택한 디바이스의 주소를 구한다
        int pos = strItem.indexOf(" - ");

        if( pos <= 0 ) return;

        String address = strItem.substring(pos + 3);
        mTextMsg.setText("Sel Device: " + address);

        // 디바이스 검색 중지
        stopFindDevice();

        // 서버 소켓 스레드 중지
        mSThread.cancel();
        mSThread = null;

        if( mCThread != null ) return;

        // 상대방 디바이스를 구한다
        BluetoothDevice device = mBA.getRemoteDevice(address);

        // 클라이언트 소켓 스레드 생성 & 시작
        mCThread = new ClientThread(device);
        mCThread.start();

        ///////////////////////////////////////////////
        //연결 후 디바이스 목록 안보이게
        ListView listview = (ListView)findViewById(R.id.listDevice);
        listview.setVisibility(View.GONE);
        listview.setVisibility(View.INVISIBLE);


        // 연결 후 메인 액티비티로 복귀!!!/////
      /*  Intent intent = new Intent(getBaseContext(), OilInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 이미실행중이면 이어서
        startActivity(intent);*/
        ///////////////////////////////////////////

    }

    // 클라이언트 소켓 생성을 위한 스레드
    private class ClientThread extends Thread {

        private BluetoothSocket mmCSocket;

        // 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
        public ClientThread(BluetoothDevice  device) {

            try {
                mmCSocket = device.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
            } catch(IOException e) {
                showMessage("Create Client Socket error");
                return;
            }
        }

        public void run() {
            // 원격 디바이스와 접속 시도
            try {
                mmCSocket.connect();
            } catch(IOException e) {
                showMessage("Connect to server error");

                // 접속이 실패했으면 소켓을 닫는다
                try {
                    mmCSocket.close();
                } catch (IOException e2) {
                    showMessage("Client Socket close error");
                }
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(mmCSocket);
        }

        // 클라이언트 소켓 중지
        public void cancel() {
            try {
                mmCSocket.close();
            } catch (IOException e) {
                showMessage("Client Socket close error");
            }
        }
    }

    // 서버 소켓을 생성해서 접속이 들어오면 클라이언트 소켓을 생성하는 스레드
    private class ServerThread extends Thread {

        private BluetoothServerSocket mmSSocket;

        // 서버 소켓 생성
        public ServerThread() {
            try {
                mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME, BLUE_UUID);
            } catch(IOException e) {
                showMessage("Get Server Socket Error");
            }
        }

        public void run() {

            BluetoothSocket cSocket = null;

            // 원격 디바이스에서 접속을 요청할 때까지 기다린다
            try {
                cSocket = mmSSocket.accept();
            } catch(IOException e) {
                showMessage("Socket Accept Error");
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(cSocket);
        }

        // 서버 소켓 중지
        public void cancel() {

            try {
                mmSSocket.close();
            } catch (IOException e) {
                showMessage("Server Socket close error");
            }
        }
    }

    // 메시지를 화면에 표시
    public void showMessage(String strMsg) {

        // 메시지 텍스트를 핸들러에 전달
        Message msg = Message.obtain(mHandler, 0, strMsg);
        mHandler.sendMessage(msg);
        Log.d("tag1", strMsg);
    }

    // 메시지 화면 출력을 위한 핸들러
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 0) {
                String strMsg = (String)msg.obj;
                mTextMsg.setText(strMsg);
            }
        }
    };

    // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
    public void onConnected(BluetoothSocket socket) {

        showMessage("Socket connected");

        // 데이터 송수신 스레드가 생성되어 있다면 삭제한다
        if( mSocketThread != null )
            mSocketThread = null;

        // 데이터 송수신 스레드를 시작
        mSocketThread = new SocketThread(socket);
        mSocketThread.start();
    }

    //////////////////////////////////////////////////////////////
    //처음, 끝 정보 서버로 보냄
    public void sending() {
        first_dis=i.obd.getDistance(); //전송할 데이터 2개
        first_fuel=i.obd.getFuel();
    }
    ///지우기
    public void last_sending() {
        km_per_liter=(i.obd.getDistance()-first_dis) /(first_fuel-i.obd.getFuel()) ;
    }

    // 가속했을때
    public boolean sending_acceleration() {

        boolean bool=false;
        long time_now=d.getTime();
        long time_interval = time_now-time_last;
        rpm_now = i.obd.getRpm();

        // 1초차이 있음
        if(time_interval >= 1){
            if((rpm_sub!=0.0) && ((rpm_now-rpm_last >= (rpm_sub/time_interval*2)))) { //급가속 했을 때
                //서버로 전송
                //i.obd.getFuel(); //전송할 데이터 3개
                //i.obd.getLatitude();
                //i.obd.getLongitude();
                bool=true;
            }
        }
        // 0초차이
        else{
            if((rpm_sub!=0.0) && ((rpm_now-rpm_last >= (rpm_sub*2)))) { //급가속 했을 때
                //서버로 전송
                //i.obd.getFuel(); //전송할 데이터 3개
                //i.obd.getLatitude();
                //i.obd.getLongitude();
                bool=true;
            }
        }

        rpm_sub = rpm_now-rpm_last;
        rpm_last = rpm_now;
        time_last=time_now;

        return bool;
    }
    ////////////////////////////////////////////////////////////////


    // 데이터 송수신 스레드
    private class SocketThread extends Thread {

        private BluetoothSocket mmSocket=null; // 클라이언트 소켓
        private InputStream mmInStream; // 입력 스트림
        ////
        private byte[] buffer = new byte[1024];
        private int bytes=0;
        private String strBuf=null;
        private double last_fuel_per=0;
        ////

        public SocketThread(BluetoothSocket socket) {

            mmSocket = socket;

            // 입력 스트림에서 데이터를 읽는다
            // 입력 스트림 구한다
            try {
                mmInStream = socket.getInputStream();
                dataParsingSet(mmInStream);
                last_fuel_per=i.obd.getFuel();

                dataParsingSet(mmInStream);
                rpm_last = i.obd.getRpm(); //처음 rpm 구하기
                time_last=d.getTime(); // 처음 수신 시간구하기 ///연비

                // 연료량(L) = 순간 사용량(L) / 사용된 양(%) * 현재남은 양(%)
                fuel_capacity=((i.obd.getFuelUse()/1000) / (i.obd.getFuel()-last_fuel_per)) * i.obd.getFuel();
                showMessage(String.format("%.3f",fuel_capacity));
                //
                //////////////////////////////////////////////////////////////////////////////////////
                ///// server 연동
                //소켓
                sending();

            } catch (IOException e) {
                showMessage("Get Stream error");
            }
        }

        // 소켓에서 수신된 데이터를 화면에 표시한다
        public void run() {
            while (dataParsingSet(mmInStream)) {
                buffer = new byte[512]; //버퍼 초기화
                SystemClock.sleep(1000);
            }
        }
        public boolean dataParsingSet(InputStream inStream){
            try {
                bytes=inStream.read(buffer);
                strBuf = new String(buffer, 0, bytes);

                //////////////////////////////////////////////////////////////////
                //파싱
                i.dataP(strBuf);

                if(sending_acceleration()) {
                    showMessage(" [ Acc! (" + i.obd.getLongitude() + ", " + i.obd.getLatitude() + ")" );
                }
                else{
                    showMessage("Receive: " + strBuf);
                }

                return true;
            }
            catch (IOException e) {
                showMessage("Socket disconnected");
                return false;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return true; // json 예외 허용
            }
        }
    }

    private class GpsThread extends Thread{
        GpsInfo gps = null;
        public GpsThread(){
        }

        public void run(){
            // GPS 사용유무 가져오기
            gps = new GpsInfo(Bluetooth_reception.this);

            if (gps.isGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                showMessage(String.format("%.3f",latitude)+"/"+String.format("%.3f",longitude));

            }
        }
    }


    // 블루투스 종료될 때
    public void onDestroy() {
        ////////////////////////////////////////
        //종료하기 전 서버로 마지막 정보 보내는 부분
        //sending();
        /////////////////////////////////////////

        super.onDestroy();

        last_sending();
        String txt = String.format("%.3f", km_per_liter);
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();

        // 디바이스 검색 중지
        stopFindDevice();////////////////////////////////////

        // 스레드를 종료
        if( mCThread != null ) {
            mCThread.cancel();
            mCThread = null;
        }

        if( mSThread != null ) {
            mSThread.cancel();
            mSThread = null;
        }

        if( mSocketThread != null )
            mSocketThread = null;

        //mBA.disable(); //블루투스 꺼짐
    }
}
