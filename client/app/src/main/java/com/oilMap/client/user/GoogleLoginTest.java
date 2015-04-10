package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.Button;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-04-10.
 */
public class GoogleLoginTest extends Activity {

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent preView = new Intent(this, LoginActivity.class);
            startActivity(preView);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    final String CLIENT_ID = "43583967498-2qsa1b1o1sf20c2qu43u8st1p9bjgoml.apps.googleusercontent.com";
    final String API_KEY = "AIzaSyAYRayqfTa7ndIth2J-RijGg4JswZ37-As";
    final String CLIENT_SECRET = "";

    final String SCOPE = "https://www.googleapis.com/auth/tasks";
    final String ENDPOINT_URL = "https://www.googleapis.com/tasks/v1/users/@me/lists";
    final String REDIRECT_URI = "http://localhost";

    String oauthUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_login);

        Button btnOauth =(Button) findViewById(R.id.testLogin);
        final WebView wvOauth = (WebView) findViewById(R.id.testWebView);

    }
}
