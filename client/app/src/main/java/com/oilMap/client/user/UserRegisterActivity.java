package com.oilMap.client.user;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class UserRegisterActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
    }

    public void mOnClick(View v){
        switch (v.getId()) {
            case R.id.btnRegNext:
                Intent carRegIntent = new Intent(this, CarRegisterActivity.class);
                startActivity(carRegIntent);
                break;
        }
    }
}
