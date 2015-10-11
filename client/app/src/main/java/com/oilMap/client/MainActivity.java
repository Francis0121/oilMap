package com.oilMap.client;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.oilMap.client.auth.Auth;
import com.oilMap.client.auth.AuthActivity_;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.info.OilInfoActivity_;
import com.oilMap.client.common.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Map;

// ~ Use Activity
@Fullscreen
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    static final String TAG = MainActivity.class.getSimpleName();

    @RestService
    AARestProtocol aaRestProtocol;

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserInfo();
    }

    @Background(delay = 1000)
    void checkUserInfo(){
        String id = userInfoPrefs.id().get();
        Log.d(TAG, "exists " + userInfoPrefs.id().exists() + " " + id);

        if(!userInfoPrefs.id().exists()){
            AuthActivity_.intent(this).start();
            finish();
        }else{
            Map<String, Object> request = new HashMap<>();
            request.put("id", id);
            Log.d(TAG, request.toString());
            Map<String, Object> response = aaRestProtocol.authSelectUrl(request);
            Log.d(TAG, response.toString());
            Map<String, Object> authMap = (Map<String, Object>) response.get("auth");
            Auth auth = new Auth((String)authMap.get("id"), (String)authMap.get("email"), (String)authMap.get("name"), "", "");
            OilInfoActivity_.intent(this).auth(auth).start();
            finish();
        }
    }
}
