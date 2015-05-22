package com.oilMap.client.bluetooth;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by 나홍철 on 2015-05-17.
 */
public class DataHandling {

    final private double OVER_ACCEL_FUEL_CONSUMPTION_MULTIPLE = 4;  // (현재연료소비/이전연료소비차이) 값이 4이상이라면 급가속
    private Context mContext;
    private TextView mTextView;
    //private GifImageView mGifImageView;
    //final private int FIXED_SUB_RPM=300; //급가속을 결정하는 rpm차이


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
    public boolean sending_acceleration(double fuel_consumption_gap, double last_fuel_consumption_gap) {
        boolean bool=false;
        //
        if( fuel_consumption_gap/last_fuel_consumption_gap >= 5  ){
           bool=true;
       }
        return bool;
    }
    /**
    처음, 끝 정보 서버로 보냄
     */
    public void sending_data_for_fuel_efficiency(double distance, double fuel) {
        new DrivingAsyncTask(this.mContext).execute(distance, fuel);
        //가속 이미지
    }

    /**
    급가속 위치 정보 서버로 보냄
     */
    public void sending_data_for_location(double latitude, double longitude, double startSpeed, double endSpeed) {
        new DrivePointAsyncTask(this.mContext).execute(latitude, longitude, startSpeed, endSpeed);
        //급가속 이미지
    }
}
