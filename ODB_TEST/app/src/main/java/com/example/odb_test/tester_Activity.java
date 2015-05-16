package com.example.odb_test;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by 나홍철 on 2015-04-25.
 */

public class tester_Activity extends Activity {


    //////////////////// Bluetooth/////////////////////

    Bluetooth bt = new Bluetooth();
    TextView mTextMsg; // 송신데이터 출력
    public String strMsg; // 송신데이터

    ////////////////////////////////////////////////////*/
    final static double BASIC_RPM = 500;
    final static double RPM_RANGE = 30.0; //rpm 증가값
    final static double FASTER_RPM_RANGE = 60.0; //rpm 증가값 기본 rpm의 2배
    final static double DECREASE_RPM = 200; // 감소되는 rpm
    final static double DECREASE_SPEED = 3; // 감소되는 속도
    final static double FUEL_EFFICIENCY = 11; // 연비 11km/L
    final static double RPM_LIMIT = 2000; // RPM 제한
    public double car_rpm = 500; // 기본 500 rpm
    public double car_speed = 0; //
    public double oil_capacity = 5000; // 5000L
    public double fuel_use = 500; // 엔진에 주입되는 기름소비량
    public double oil_consumption = 0; // 기름소비량
    public double distance = 1000; // 현재 1000km를 탄상태
    public int current_gear = 0;

    TextView fuel_use_text;
    TextView rpm_text;
    TextView gear_text;
    TextView speed_text;
    TextView oilConsumption_text;
    TextView distance_text;

    Timer time_timer = new Timer(); // 변경사항 보여주는 쓰레드 실행을 위한 타이머

    public boolean static_flag = false;   // 정속주행 ture
    public boolean acc_flag = false;
    public boolean faster_acc_flag = false;
    public boolean brk_flag = false;
    public double GEAR_RATIO[] = {4.580, 2.960, 1.910, 1.450, 1.000};    // 5단 기어비 기어의 톱니수 비율
    /////예를 들어 1단의 기어비가 4:1이고 여기에 종감속비가 4:1이라면 전체기어비는 16:1이 되고 이는 엔진이 16번 회전해야 타이어가 1번 회전한다고 볼 수 있다.

    //double GEAR_RATIO[] = {2.580, 2.120, 1.830, 1.450, 1.000};    // 5단 기어비 기어의 톱니수 비율
    public double REDUCTION_GEAR_RATIO = 2.890;
    public double SHIFT_GEAR_SPEED[] = {0, 20, 40, 60, 80}; // 0~20 1단 , 20~40 2단..


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tester_activity);

        //////////////// Bluetooth///////////////////////////////////
        mTextMsg = (TextView) findViewById(R.id.bluetooth);

        // 블루투스 사용 가능상태 판단
        boolean isBlue = bt.canUseBluetooth();
        if (isBlue)
            // 블루투스 수신 서버쓰레드 생성
            bt.getServerThread();
        ////////////////////////////////////////////////////////////////
        fuel_use_text = (TextView) findViewById(R.id.fueluseText);
        rpm_text = (TextView) findViewById(R.id.rpmText);
        gear_text = (TextView) findViewById(R.id.gearText);
        speed_text = (TextView) findViewById(R.id.spdText);
        oilConsumption_text = (TextView) findViewById(R.id.oilconText);
        distance_text = (TextView) findViewById(R.id.distanceText);

        time_timer.schedule(timeTimerTask, 10, 1000);    // TEXT 실시간 표시
        time_timer.schedule(rpmTimeTask, 10, 1000); // accel 플래그 있다면 수행
        time_timer.schedule(speedTimeTask, 10, 1000); // brake 플래그 있다면 수행

        // this goes wherever you setup your button listener:

        Button accelerationButton = (Button) findViewById(R.id.accelBtn);
        Button fasterAccelerationButton = (Button) findViewById(R.id.fasterAccelBtn);
        Button brakeButton = (Button) findViewById(R.id.brakeBtn);
        Button staticSpeedButton = (Button) findViewById(R.id.staticSpeedBtn);
        Button backButton = (Button) findViewById(R.id.backBtn);

        // Speed Up Button
        accelerationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                static_flag = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    acc_flag = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    acc_flag = false;
                    return true;
                }
                return false;
            }
        }); // Speed Up more faster Button
        fasterAccelerationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                static_flag = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    faster_acc_flag = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    faster_acc_flag = false;
                    return true;
                }
                return false;
            }
        });
        // Speed Down Button
        brakeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                static_flag = false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    brk_flag = true;
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    brk_flag = false;
                    return true;
                }
                return false;
            }
        });
        // Static SppedBytton
        staticSpeedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                static_flag = true;
                Toast.makeText(getApplicationContext(), "Static Speed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //  Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Back",
                        Toast.LENGTH_SHORT).show();

                Intent tester_Intent = new Intent(getBaseContext(), Obd_Tester.class);
                tester_Intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); // 이미실행중이면 이어서
                startActivity(tester_Intent);
              /*  moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());*/
            }
        });

    }

    // rpm 관리
    TimerTask rpmTimeTask = new TimerTask() {
        public void run() {
            // 정속주행이 아니라면
            if (!static_flag) {
                // 악셀중일때
                if (acc_flag) {
                    //rpm은 최대치를 넘지 않는다.
                    car_rpm = (car_rpm < (RPM_LIMIT - RPM_RANGE * GEAR_RATIO[current_gear])) ? car_rpm + RPM_RANGE * GEAR_RATIO[current_gear] : car_rpm;
                    // 엔진에 주입되는 기름
                    fuel_use = (fuel_use < (RPM_LIMIT - RPM_RANGE)) ? fuel_use + RPM_RANGE : fuel_use;
                    // 가속구간
                } else if (faster_acc_flag) {
                    car_rpm = (car_rpm < (RPM_LIMIT - FASTER_RPM_RANGE * GEAR_RATIO[current_gear])) ? car_rpm + FASTER_RPM_RANGE * GEAR_RATIO[current_gear] : car_rpm;// 기름 소비량
                    fuel_use = (fuel_use < (RPM_LIMIT - FASTER_RPM_RANGE)) ? fuel_use + FASTER_RPM_RANGE : fuel_use;
                }
                // 가속중이지 않다면 rpm은 줄어든다.
                else {
                    car_rpm = (car_rpm < BASIC_RPM + DECREASE_RPM / GEAR_RATIO[current_gear]) ? BASIC_RPM : car_rpm - DECREASE_RPM / GEAR_RATIO[current_gear];
                    fuel_use = (fuel_use < BASIC_RPM + DECREASE_RPM) ? BASIC_RPM : fuel_use - DECREASE_RPM;
                }
            }
        }
    };
    // speed 관리
    TimerTask speedTimeTask = new TimerTask() {
        public void run() {

            double rpm_speed = 0; // rpm 속도 : 차속(km/h)= 2π ×타이어반지름 ×(엔진rpm)/(변속기 기어비 × 종감속 기어비) × 60/1000
            // 브레이크 중 일때 차 속도는 0미만이 되지 못한다.
            rpm_speed = 2 * 3.14 * car_rpm / (GEAR_RATIO[current_gear] * REDUCTION_GEAR_RATIO) * 60 / 1000; // 엔진속도

            if (brk_flag) {
                car_speed = (car_speed > DECREASE_SPEED) ? car_speed - DECREASE_SPEED : 0;
            } else {
                car_speed = (car_speed > rpm_speed) ? car_speed : rpm_speed; // 현재속도와 엔진이 주는 속도 비교하여 빠른속도 선택
            }

            if (car_speed >= DECREASE_SPEED)
                car_speed -= DECREASE_SPEED; // 줄어드는 속도
            else if (car_speed < DECREASE_SPEED && car_speed >= 0)
                car_speed = 0;
            ////////

            // 5단까지 변속 가능
            if ((current_gear < 4) && (car_speed > SHIFT_GEAR_SPEED[current_gear + 1])) {
                current_gear++;
                // 이전기어에서의 rpm보다 현제 기어에서 더 많은 회전이 필요하므로
                car_rpm = car_rpm * GEAR_RATIO[current_gear] / GEAR_RATIO[current_gear - 1];
            } else if ((current_gear > 0) && (car_speed < SHIFT_GEAR_SPEED[current_gear])) {
                current_gear--;
                car_rpm = car_rpm * GEAR_RATIO[current_gear] / GEAR_RATIO[current_gear + 1];
            }
            ////////

            // 순간속도 1초 만큼 거리 추가
            distance += car_speed / 3600;
        }
    };
    // 매초 속도와 기름소비량 정보를 계산하여 보여준다.
    TimerTask timeTimerTask = new TimerTask() {
        public void run() {

            oil_consumption = fuel_use / 60 / FUEL_EFFICIENCY;  // 1초당 rpm 따른 기름소비량 ///// 수정!!!!!!

            // rpm따른 남은 기름량이 0보다 커야 계산가능
            if ((car_rpm > 0) && (oil_capacity > 0)) {
                oil_capacity -= oil_consumption;
            }
            // Fuel consumption
            Handler fueluseHandler = fuel_use_text.getHandler();
            if (fueluseHandler != null) {
                fueluseHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "fuel use : " + fuel_use);
                        fuel_use_text.setText(Double.toString(Double.parseDouble(String.format("%.1f", fuel_use))));
                    }
                });
            }
            // RPM
            Handler rpmHandler = rpm_text.getHandler();
            if (rpmHandler != null) {
                rpmHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "rpm : " + car_rpm);
                        rpm_text.setText(Double.toString(Double.parseDouble(String.format("%.1f", car_rpm))));
                    }
                });
            }
            // Gear
            Handler gearHandler = gear_text.getHandler();
            if (gearHandler != null) {
                gearHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "gear : " + current_gear);
                        gear_text.setText(Double.toString(Double.parseDouble(String.format("%d", current_gear + 1))));
                    }
                });
            }

            // Speed
            Handler speedHandler = speed_text.getHandler();
            if (speedHandler != null) {
                speedHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "speed : " + car_speed);
                        speed_text.setText(Double.toString(Double.parseDouble(String.format("%.3f", car_speed))));
                    }
                });
            }

            // Oil capacity
            Handler oilHandler = oilConsumption_text.getHandler();
            if (oilHandler != null) {
                oilHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "oil : " + oil_capacity);
                        oilConsumption_text.setText(Double.toString(Double.parseDouble(String.format("%.3f", oil_capacity))));
                    }
                });
            }
            // distnace
            Handler disHandler = distance_text.getHandler();
            if (oilHandler != null) {
                oilHandler.post(new Runnable() {
                    public void run() {
                        Log.d("timeTimerTask", "distance : " + distance);
                        distance_text.setText(Double.toString(Double.parseDouble(String.format("%.3f", distance))));
                    }
                });
            }
            // Sendin Message
            Handler mHandler = mTextMsg.getHandler();
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mTextMsg.setText(strMsg);
                    }
                });
            }
        } //end run
    }; //end TimerTask

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ///////////Bluetooth/////////////
        if (bt.mSThread != null) {
            bt.mSThread.cancel();
            bt.mSThread = null;
        }

        if (bt.mSocketThread != null) {
            bt.mSocketThread = null;
        }
        ////////////////////////////////////////
        time_timer.cancel();
        timeTimerTask.cancel();
        rpmTimeTask.cancel();
        speedTimeTask.cancel();

        finish();
    }
    /////////////////////////////////////////////////////

    ///////////////// Bluetooth//////////////////////////
    class Bluetooth {
        static final String BLUE_NAME = "BluetoothEx";  // 접속시 사용하는 이름
        // 접속시 사용하는 고유 ID
        final UUID BLUE_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
        Json_Data jd = new Json_Data();   // Json data 객체 생성
        BluetoothAdapter mBA;
        ServerThread mSThread = null; // 서버 소켓 접속 스레드
        SocketThread mSocketThread = null; // 데이터 송수신 스레드

        // 블루투스 사용 가능상태 판단
        public boolean canUseBluetooth() {

            // 블루투스 어댑터를 구한다
            mBA = BluetoothAdapter.getDefaultAdapter();

            // 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
            if (mBA == null) {
                showMessage("Device not found");
                return false;
            }

            showMessage("Device is exist");

            // 블루투스 활성화 상태라면 함수 탈출
            if (mBA.isEnabled()) {
                showMessage("\nDevice can use");
                return true;
            }

            return false;
        }


        // 다른 디바이스에게 자신을 검색 허용
        public void setDiscoverable() {

            // 현재 검색 허용 상태라면 함수 탈출
            if (mBA.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
                return;

            // 다른 디바이스에게 자신을 검색 허용 지정
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(intent);
        }

        // 블루투스 수신 서버쓰레드 생성
        public void getServerThread() {

            if (mSThread != null) return;

            // 서버 소켓 접속을 위한 스레드 생성 & 시작
            mSThread = new ServerThread();
            mSThread.start();

            // 다른 디바이스에 자신을 노출
            setDiscoverable();
        }

        // 메시지를 화면에 표시
        public void showMessage(String msg) {
            strMsg = msg;//(String)msg.obj;
            Log.d("tag1", strMsg);
        }

        // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
        public void onConnected(BluetoothSocket socket) {

            showMessage("Socket connected");

            // 데이터 송수신 스레드가 생성되어 있다면 삭제한다
            if (mSocketThread != null)
                mSocketThread = null;

            // 데이터 송수신 스레드를 시작
            mSocketThread = new SocketThread(socket);
            mSocketThread.start();
        }

        // 서버 소켓을 생성해서 접속이 들어오면 클라이언트 소켓을 생성하는 스레드
        private class ServerThread extends Thread {

            private BluetoothServerSocket mmSSocket;

            // 서버 소켓 생성
            public ServerThread() {
                try {
                    mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME, BLUE_UUID);
                } catch (IOException e) {
                    showMessage("Get Server Socket Error");
                }
            }

            public void run() {

                BluetoothSocket cSocket;

                // 원격 디바이스에서 접속을 요청할 때까지 기다린다
                while (true) {
                    try {
                        cSocket = mmSSocket.accept();
                    } catch (IOException e) {
                        showMessage("Socket Accept Error");
                        break;
                    }
                    // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
                    if (cSocket != null) {
                        onConnected(cSocket);
                    }
                }
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

        // 데이터 송수신 스레드
        private class SocketThread extends Thread {

            private OutputStream mmOutStream; // 출력 스트림

            public SocketThread(BluetoothSocket socket) {
                // 입력 스트림과 출력 스트림을 구한다
                try {
                    mmOutStream = socket.getOutputStream();
                } catch (IOException e) {
                    showMessage("Get Stream error");
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            // 소켓에서 수신된 데이터를 화면에 표시한다
            public void run() {
                while (true) {
                    jd.setRpm(Double.parseDouble(String.format("%.3f", car_rpm)));
                    jd.setFuel(Double.parseDouble(String.format("%.3f", oil_capacity)));
                    jd.setDistance(Double.parseDouble(String.format("%.3f", distance)));
                    // 아웃 스트림 json 객체의 스트링을 반환받아 작성
                    if (mSocketThread.write(jd.retJson()))
                        showMessage("Send: " + jd.getRpm() + "/" + jd.getFuel());// + strBuf);
                    else
                        showMessage("Socket Disconnected");
                    SystemClock.sleep(1000);
                }

            }

            // 데이터를 소켓으로 전송한다
            public boolean write(String strBuf) {
                try {
                    // 출력 스트림에 데이터를 저장한다
                    byte[] buffer = strBuf.getBytes();
                    mmOutStream.write(buffer);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }
}

