package com.oilMap.client.user;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class UserRegisterActivity extends Activity {

    User user = new User();
    private boolean keySuccess = false;

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
                new UserRegisterAsyncTask().execute(user);
                Intent carRegIntent = new Intent(this, CarRegisterActivity.class);
                if(keySuccess) {
                    startActivity(carRegIntent);
                    finish();
                }
                break;

            case R.id.btnRegUserClear:
                editID.setText("");
                editPW.setText("");
                editRePW.setText("");
                editEmail.setText("");
                break;
        }
    }

    private class UserRegisterAsyncTask extends AsyncTask<User, Void, Map<String, Object>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, Object> doInBackground(User... users) {

            if(users[0] == null){
                return null;
            }

            try {
                String url = getString(R.string.contextPath) + "/user/join";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, users[0], Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;

            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            Log.d("join", map.toString());

            if((Boolean)map.get("success")){
                keySuccess = true;
                String joinYes = "회원가입에 성공하였습니다";
                Toast.makeText(UserRegisterActivity.this, joinYes, Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                map.get("messages");
                Map<String,Object> regMap = (Map<String, Object>) map.get("messages");

                TextView idView = (TextView) findViewById(R.id.IDView);
                idView.setText(regMap.get("username").toString());
                idView.setTextColor(0xffff0000);

                TextView pwView = (TextView) findViewById(R.id.PWView);
                pwView.setText(regMap.get("password").toString());
                pwView.setTextColor(0xffff0000);

                TextView emilView = (TextView) findViewById(R.id.EmailView);
                emilView.setText(regMap.get("email").toString());
                emilView.setTextColor(0xffff0000);
            }
        }
    }
}



