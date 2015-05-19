package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-15.
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.MapsInitializer;
import com.oilMap.client.R;
import com.oilMap.client.gps.GpsInfo;
import com.oilMap.client.info.OilInfoActivity;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


public class Bluetooth_reception extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Bluetooth_reception";

    public DataParsing i = new DataParsing(); // Data Parsing Class

    /********************** Bluetooth *************************************/
    static final int ACTION_ENABLE_BT = 101;

    BluetoothAdapter mBA;
    ListView mListDevice;
    ArrayList<String> mArDevice; // 원격 디바이스 목록

    static final String  BLUE_NAME = "BluetoothEx";  // 접속시 사용하는 이름
    // 접속시 사용하는 고유 ID
    static final UUID BLUE_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    Thread mCThread = null; // 클라이언트 소켓 접속 스레드
    Thread mSocketThread = null; // 데이터 송수신 스레드

    //급가속 시 서버로 보냄/////
    ////지우기
    public double rpm_last=0.0;
    public int rpm_sub=999;
    public long time_last=0;
    public Date d=new Date();
    double latitude;
    double longitude;

    /****************Data Handling*********************/
    TextView mTextMsg, mRunTextView;
    DataHandling data_handling=null;
    //GifImageView gifImageView;
    /****************GPS*********************/
    GpsInfo gps = null;


    private void setSharedPreference(String status, String imageType){
        Log.d(TAG, "setSharedPreference " + status + " " + imageType);
        SharedPreferences pref = getSharedPreferences("socket", 0);
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putString("status", status);
        if(imageType != null) {
            prefEdit.putString("imageType", imageType);
        }
        prefEdit.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.gps_main);


        /********************GPS*************************/
        MapsInitializer.initialize(getApplicationContext());
        init();
        gps = new GpsInfo(Bluetooth_reception.this);

        /*************************DataHandling**********************/
        mTextMsg = (TextView)findViewById(R.id.textMessage);
        //gifImageView = (GifImageView) findViewById(R.id.carImageView);
        mRunTextView = (TextView) findViewById(R.id.carRunTextView);
        data_handling=new DataHandling(Bluetooth_reception.this, mTextMsg, mRunTextView);

        /********************** Bluetooth *************************************/

        mBA = BluetoothAdapter.getDefaultAdapter();
        initListView();  // ListView 초기화
        // 블루투스 사용 가능상태 판단
        boolean isBlue = canUseBluetooth();
        if( isBlue )
            // 페어링된 원격 디바이스 목록 구하기
            getParedDevice();
        /******************************************************************************/

        /*********************BackBtn****************************************************/

        Button backButton = (Button) findViewById(R.id.BackPageBtn);
        //  Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OilInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 이미실행중이면 이어서
                startActivity(intent);
                //
            }});
        /**********************************************************************************/
        //setSharedPreference("2", "1");
    }

    private void init() {

        GooglePlayServicesUtil.isGooglePlayServicesAvailable(Bluetooth_reception.this);
        // 맵의 이동
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        GpsInfo gps = new GpsInfo(Bluetooth_reception.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

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

        data_handling.showMessage("Bluetooth 장치 OBD를 선택해주세요.");

        // 디바이스 검색 시작
        mBA.startDiscovery();

        // 원격 디바이스 검색 이벤트 리시버 등록
        registerReceiver(mBlueRecv, new IntentFilter(BluetoothDevice.ACTION_FOUND));
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

        if( mCThread != null ) return;

        // 상대방 디바이스를 구한다
        BluetoothDevice device = mBA.getRemoteDevice(address);

        // 클라이언트 소켓 스레드 생성 & 시작
        mCThread = new ClientThread(device);
        mCThread.start();

        ///////////////////////////////////////////////
        //연결 후 디바이스 목록 안보이게
//        mListDevice.setVisibility(View.GONE);
//        mListDevice.setVisibility(View.INVISIBLE);

        RelativeLayout relativeLayoutListView= (RelativeLayout) findViewById(R.id.relativeLayoutListView);
        relativeLayoutListView.setVisibility(View.GONE);

        RelativeLayout relativeLayoutRunImage = (RelativeLayout) findViewById(R.id.relativeLayoutRunImage);
        relativeLayoutRunImage.setVisibility(View.VISIBLE);

        TextView runingText = (TextView) findViewById(R.id.carRunTextView);

        /************************* 연결 후 메인 액티비티로 복귀!!!/////************************/
        Intent intent = new Intent(getBaseContext(), OilInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 이미실행중이면 이어서
        startActivity(intent);
        ///////////////////////////////////////////

    }

    // 클라이언트 소켓 생성을 위한 스레드
    private class ClientThread extends Thread {

        private BluetoothSocket mmCSocket;

        // 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
        public ClientThread(BluetoothDevice device) {

            try {
                mmCSocket = device.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
            } catch(IOException e) {
                data_handling.showMessage("Create Client Socket error");
                return;
            }
        }

        public void run() {
            // 원격 디바이스와 접속 시도
            try {
                mmCSocket.connect();
            } catch(IOException e) {
                data_handling.showMessage("Connect to server error");

                // 접속이 실패했으면 소켓을 닫는다
                try {
                    mmCSocket.close();
                } catch (IOException e2) {
                    data_handling.showMessage("Client Socket close error");
                }
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(mmCSocket);
        }
        // 클라이언트 소켓 중지
        public void interrupt() {
            try {
                mmCSocket.close();
            } catch (IOException e) {
                data_handling.showMessage("Client Socket close error");
            }
        }
    }

    // 데이터 송수신 스레드
    private class SocketThread extends Thread {

        private BluetoothSocket mmSocket=null; // 클라이언트 소켓
        private InputStream mmInStream; // 입력 스트림
        ////
        private byte[] buffer = new byte[1024];
        private int bytes=0;
        private String strBuf=null;
        ////

        public SocketThread(BluetoothSocket socket) {

            mmSocket = socket;

            // 입력 스트림에서 데이터를 읽는다
            // 입력 스트림 구한다
            try {
                mmInStream = socket.getInputStream();

            } catch (IOException e) {
                data_handling.showMessage("Get Stream error");
            }
        }

        // 소켓에서 수신된 데이터를 화면에 표시한다
        public void run() {
            setSharedPreference("1", "2");
            // 처음 자료 보내기
            while (true) {
                // 첫 데이터가 정상적이면서 rpm이 0보다 크면 초기 값으로 설정과 전송후 반복문나간다
                if(dataParsingSet(mmInStream) && (i.obd.getRpm() > 0) && (i.obd.getDistance() > 0) && (i.obd.getFuel() > 0 )) {
                    rpm_last = i.obd.getRpm(); //처음 rpm 구하기
                    time_last=d.getTime(); // 처음 수신 시간구하기 ///연비

                    //처음 연비 보내기!!!!!!//////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    data_handling.sending_data_for_fuel_efficiency(i.obd.getDistance(),i.obd.getFuel());
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    //////////////////////////////////////////////////////////////////////////////////////////////////////
                    break;
                }
            }

            // 데이터 받기
            while (dataParsingSet(mmInStream)) {
                rpmCompare();

                buffer = new byte[512]; //버퍼 초기화
                SystemClock.sleep(1000);
            }
        }
        // 수신받은 데이터를 파싱하여 obd객체에 셋팅
        public boolean dataParsingSet(InputStream inStream){
            try {
                bytes=inStream.read(buffer);
                strBuf = new String(buffer, 0, bytes);

                i.dataP(strBuf);

                return true;
            }
            // OBD와 접속 끊김
            catch (IOException e) {
                data_handling.showMessage("Socket disconnected");
                initListView();  // ListView 초기화
                //OBD접속 끊길때 연비 보내기!!!!!!//////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                data_handling.sending_data_for_fuel_efficiency(i.obd.getDistance(), i.obd.getFuel()); // 종료전 자료전송
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////

                setSharedPreference("0", "1");
                return false;
            }
            catch (JSONException e) {
                e.printStackTrace();
                return true; // json 예외 허용
            }
        }
        // rpm 증가 차이를 비교하여 급가속 위치 구분
        public void rpmCompare(){
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////
            // 급가속시 위치 데이터 셋팅//////////////////////////////////////////////////////////////////////////
            if(data_handling.sending_acceleration(rpm_sub,i.obd.getTime(),time_last,i.obd.getRpm(),rpm_last)) {
                i.obd.setLatitude(latitude);
                i.obd.setLongitude(longitude);
                data_handling.showMessage(" [ Acc! (" + i.obd.getLongitude() + ", " + i.obd.getLatitude() + ")" +rpm_last+"/"+i.obd.getRpm()+"/"+ rpm_sub );
                data_handling.sending_data_for_location(latitude,longitude, rpm_last,i.obd.getRpm());
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////////////////////////////////////////////
                setSharedPreference("2", "1");
            }
            else{
                data_handling.showMessage("Receive: " + "연비:"+ i.obd.getFuelEfficiency()+"/ 연료:"
                        + i.obd.getFuel() +"/ RPM:" + i.obd.getRpm() + "/ 연료%" +i.obd.getFuelLevel() + "/ 시간:"
                        + i.obd.getTime() + "/ 거리:" + i.obd.getDistance() +"/" +rpm_sub);
            }

            // 다음 연산을 위함
            rpm_sub = (int)(i.obd.getRpm()-rpm_last);
            rpm_last = i.obd.getRpm();
            time_last = i.obd.getTime();
        }
    }
    // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
    public void onConnected(BluetoothSocket socket) {

        data_handling.showMessage("Socket connected");

        // 데이터 송수신 스레드가 생성되어 있다면 삭제한다
        if( mSocketThread != null )
            mSocketThread = null;

        // 데이터 송수신 스레드를 시작
        mSocketThread = new SocketThread(socket);
        mSocketThread.start();
    }


    // 블루투스 종료될 때
    public void onDestroy() {
        super.onDestroy();

        // 디바이스 검색 중지
        stopFindDevice();////////////////////////////////////

        // 스레드를 종료
        if( mCThread != null ) {
            mCThread.interrupt();
            mCThread = null;
        }

        if( mSocketThread != null )
            mSocketThread = null;

        //mBA.disable(); //블루투스 꺼짐
        //setSharedPreference("0", "1");
    }

}
