package com.oilMap.client.user;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class UserRegisterActivity extends Activity {

    User user = new User();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
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

        EditText editID = (EditText) findViewById(R.id.regId);
        EditText editPW = (EditText) findViewById(R.id.regPw);
        EditText editRePW = (EditText) findViewById(R.id.regRePw);
        EditText editEmail = (EditText) findViewById(R.id.regEmail);

        user.setUsername(editID.getText().toString());
        user.setPassword(editPW.getText().toString());
        user.setConfirmPassword(editRePW.getText().toString());
        user.setEmail(editEmail.getText().toString());

        switch (v.getId()) {
            case R.id.btnRegNext:
                Intent carRegIntent = new Intent(this, CarRegisterActivity.class);
                startActivity(carRegIntent);
                finish();
                break;

            case R.id.btnRegUserClear:
                editID.setText("");
                editPW.setText("");
                editRePW.setText("");
                editEmail.setText("");
                break;
        }
    }
}
