package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.oilMap.client.R;
import com.oilMap.client.info.NavigationActivity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class LoginActivity extends Activity {

    private BackPressCloseHandler backPressCloseHandler;
    private String username;
    private String password;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public  void onBackPressed(){
        backPressCloseHandler.onBackpressed();
    }


    public void mOnClick(View v) {
        // View v 에 어떤정보를 클릭했는가 하는 정보가 들어있다.
        EditText edittxt = (EditText) findViewById(R.id.txtid);
        EditText edittxt1 = (EditText) findViewById(R.id.txtpw);
        username = edittxt.getText().toString();
        password = edittxt1.getText().toString();

        switch (v.getId()) {
            // 클릭한 버튼의 아이디가 리턴된다.
            case R.id.btnLogin:
                User user = new User(username, password);
                new LoginAsyncTask().execute(user);
                break;

            case R.id.btnRegister:
                Intent userRegIntent = new Intent(this, UserRegisterActivity.class);
                startActivity(userRegIntent);
                finish();
                break;

            case R.id.btnFind:
                Intent findInform = new Intent(this, FindInformationActivity.class);
                startActivity(findInform);
                finish();
                break;

        }
    }

    /**
     * Login Async Task class
     */
    private class LoginAsyncTask extends AsyncTask<User, Void, Map<String, Object>>{

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
                String url = getString(R.string.contextPath) + "/user/login";
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
            Log.d("login", map.toString());

            if((Boolean)map.get("success")){
                String loginyes = "로그인에 성공하였습니다.";
                Toast.makeText(LoginActivity.this, loginyes, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }else{
                String loginno = "로그인에 실패하였습니다." + map.get("messages").toString();
                Toast.makeText(LoginActivity.this, loginno, Toast.LENGTH_SHORT).show();
            }

        }
    }
}