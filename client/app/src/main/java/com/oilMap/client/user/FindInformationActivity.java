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

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class FindInformationActivity extends Activity {

    User user = new User();

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

        EditText editID = (EditText) findViewById(R.id.findID);
        EditText editEmail = (EditText) findViewById(R.id.findEmail);

        user.setUsername(editID.getText().toString());
        user.setEmail(editEmail.getText().toString());

        switch (v.getId()) {

            case R.id.btnFindComplete:
                new FindInformAsyncTask().execute(user);
                break;

            case R.id.btnFindExit:
                Intent findExit = new Intent(this, LoginActivity.class);
                startActivity(findExit);
                finish();
                break;
        }
    }

    private class FindInformAsyncTask extends AsyncTask<User, Void, Map<String, Object>> {

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
                String url = getString(R.string.contextPath) + "/user/updateNewPassword";
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
            Log.d("updateNewPassword", map.toString());

            //정보 일치 시 새로운 비밀번호 뷰에 표시
            if((Boolean)map.get("success")){

                if(map.get("newPassword") != null){
                    TextView newPasswordView = (TextView) findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.VISIBLE);
                    newPasswordView.setText(map.get("newPassword").toString());
                    newPasswordView.setTextColor(0xffff0000);
                }
                else{
                    TextView newPasswordView = (TextView) findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.INVISIBLE);
                }
            }

            //입력정보 오류 or 등록되지 않은 정보
            else{

                if(map.get("messages") != null){
                    TextView newPasswordView = (TextView) findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.VISIBLE);
                    newPasswordView.setText(map.get("messages").toString());
                    newPasswordView.setTextColor(0xffff0000);
                }

                else{
                    TextView newPasswordView = (TextView) findViewById(R.id.newPasswordView);
                    newPasswordView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
