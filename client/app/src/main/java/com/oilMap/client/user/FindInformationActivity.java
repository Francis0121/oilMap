package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.oilMap.client.R;

public class FindInformationActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent preView = new Intent(this, LoginActivity.class);
            startActivity(preView);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void mOnClick(View v){
        switch (v.getId()) {
            case R.id.btnFindExit:
                Intent findExit = new Intent(this, LoginActivity.class);
                startActivity(findExit);
                finish();
                break;
        }
    }
}
