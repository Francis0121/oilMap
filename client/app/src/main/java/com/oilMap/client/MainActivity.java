package com.oilMap.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.oilMap.client.auth.Auth;
import com.oilMap.client.auth.AuthActivity;
import com.oilMap.client.info.OilInfoActivity;
import com.oilMap.client.rest.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Fullscreen
@EActivity(R.layout.main)
public class MainActivity extends Activity {

    static final String TAG = MainActivity.class.getSimpleName();

    @RestService
    AARestProtocol aaRestProtocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserInformationFromSharedPreference();
    }

    @Background(delay = 1000)
    void getUserInformationFromSharedPreference(){

        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        String userId = pref.getString("id", "");

        if(userId == null || userId.equals("")){
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }else{
            Map<String, Object> request = new HashMap<>();
            request.put("id", userId);
            Log.d(TAG, request.toString());

            Map<String, Object> response = aaRestProtocol.authSelectUrl(request);
            Log.d(TAG, response.toString());

            Map<String, Object> authMap = (Map<String, Object>) response.get("auth");
            Auth auth = new Auth((String)authMap.get("id"), (String)authMap.get("email"), (String)authMap.get("name"), "", "");

            Intent intent = new Intent(this, OilInfoActivity.class);
            intent.putExtra("auth", auth);
            startActivity(intent);
            finish();
        }
    }
}
