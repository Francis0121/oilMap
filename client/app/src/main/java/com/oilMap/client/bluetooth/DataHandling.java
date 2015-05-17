package com.oilMap.client.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.oilMap.client.gps.GpsInfo;

/**
 * Created by 나홍철 on 2015-05-17.
 */
public class DataHandling {

    private Context mContext;
    private TextView mTextView;

    public DataHandling(Context mContext, TextView mTextView) {
        this.mTextView = mTextView;
        this.mContext = mContext;
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
                mTextView.setText(strMsg);
            }
        }
    };

    /**
    * 급가속시 실행
    */
    public boolean sending_acceleration(int rpm_sub, long time_now, long time_last, double rpm_now, double rpm_last) {
        boolean bool=false;
        long time_interval =0;
        time_interval = (time_now-time_last)>1 ? (time_now-time_last):1;
//
//        // 1초차이 있음
        if((rpm_sub >= 0.0) && ((rpm_now-rpm_last > ((rpm_sub/time_interval)*2)))) { //급가속 했을 때
           bool=true;
       }

        return bool;
    }
    /**
    처음, 끝 정보 서버로 보냄
     */
    public void sending_data_for_fuel_efficiency(double distance, double fuel) {
        new DrivingAsyncTask(this.mContext).execute(distance, fuel);
    }

    /**
    급가속 위치 정보 서버로 보냄
     */
    public void sending_data_for_location(double latitude, double longitude) {
        Double startSpeed = 0.0;
        Double endSpeed = 0.0;
        new DrivePointAsyncTask(this.mContext).execute(latitude, longitude, startSpeed, endSpeed);
    }
}
