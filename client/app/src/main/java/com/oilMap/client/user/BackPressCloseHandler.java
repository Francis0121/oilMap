package com.oilMap.client.user;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by 김현준 on 2015-03-26.
 */
public class BackPressCloseHandler {

    private long backKeyPressTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context){
        this.activity = context;
    }

    public void onBackpressed(){
        if(System.currentTimeMillis() > backKeyPressTime + 2000){
            backKeyPressTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressTime + 2000){
            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();
        }
    }

    public void showGuide(){
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다." , Toast.LENGTH_SHORT);
        toast.show();
    }
}
