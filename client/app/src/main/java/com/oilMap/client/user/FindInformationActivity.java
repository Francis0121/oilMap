package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oilMap.client.R;

public class FindInformationActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
    }
    public void mOnClick(View v){
        switch (v.getId()) {
            case R.id.btnFindExit:
                Intent findExit = new Intent(this, LoginActivity.class);
                startActivity(findExit);
                break;
        }
    }
}
