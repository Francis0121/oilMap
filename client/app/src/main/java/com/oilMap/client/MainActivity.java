package com.oilMap.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.oilMap.client.auth.Auth;
import com.oilMap.client.auth.AuthActivity_;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.info.OilInfoActivity_;
import com.oilMap.client.rest.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Map;

@Fullscreen
@EActivity(R.layout.main)
public class MainActivity extends Activity {

    static final String TAG = MainActivity.class.getSimpleName();

    @RestService
    AARestProtocol aaRestProtocol;

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserInformationFromSharedPreference();
    }

    @Background(delay = 1000)
    void getUserInformationFromSharedPreference(){

        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        String userId = pref.getString("id", "");

        userInfoPrefs.id().exists();
        String id = userInfoPrefs.id().get();
        Log.d(TAG, "exists " + userInfoPrefs.id().exists() + " " + id);

        if(userId == null || userId.equals("")){
            AuthActivity_.intent(this).start();
            finish();
        }else{
            Map<String, Object> request = new HashMap<>();
            request.put("id", userId);
            Log.d(TAG, request.toString());

            Map<String, Object> response = aaRestProtocol.authSelectUrl(request);
            Log.d(TAG, response.toString());

            Map<String, Object> authMap = (Map<String, Object>) response.get("auth");
            Auth auth = new Auth((String)authMap.get("id"), (String)authMap.get("email"), (String)authMap.get("name"), "", "");

            Intent intent = new Intent(this, OilInfoActivity_.class);
            intent.putExtra("auth", auth);
            startActivity(intent);
            finish();
        }
    }
}
