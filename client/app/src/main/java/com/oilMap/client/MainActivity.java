package com.oilMap.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.oilMap.client.auth.Auth;
import com.oilMap.client.auth.AuthActivity;
import com.oilMap.client.info.NavigationActivity;
import com.oilMap.client.info.OilInfoActivity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ID check
                SharedPreferences pref = getSharedPreferences("userInfo", 0);
                String userId = pref.getString("id", "");

                if(userId == null || userId.equals("")){
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                   // ~ 서버로부터 정보 조회
                   new AuthInfoAsnycTask().execute(userId);
                }
            }
        }, 1000);
    }

    private class AuthInfoAsnycTask extends AsyncTask<String, Void, Map<String, Object>>{

        private Map<String, Object> request = new HashMap<String, Object>();

        @Override
        protected Map<String, Object> doInBackground(String... ids) {
            if(ids.length < 1){
                throw new RuntimeException("Id is null");
            }

            try {
                String url = getString(R.string.contextPath) + "/auth/select";
                request.put("id", ids[0]);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                throw new RuntimeException("Communication error occur");
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> respones) {
            super.onPostExecute(respones);
            Log.d(TAG, respones.toString());

            if((Boolean)respones.get("result")){
                Map<String, Object> authMap = (Map<String, Object>) respones.get("auth");
                Auth auth = new Auth((String)authMap.get("id"), (String)authMap.get("email"), (String)authMap.get("name"), "", "");
                Log.d(TAG, respones.toString());

                Intent intent = new Intent(MainActivity.this, OilInfoActivity.class);
                intent.putExtra("auth", auth);
                startActivity(intent);
                finish();
            }else{
                throw new RuntimeException("Auth select communication failed");
            }
        }
    }
}
