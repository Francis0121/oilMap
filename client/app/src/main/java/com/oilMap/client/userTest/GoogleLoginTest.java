package com.oilMap.client.userTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.oilMap.client.R;
import com.oilMap.client.user.LoginActivity;

import java.io.IOException;

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

    String CLIENT_ID = "730145234541-dkjd04q3r5gml2vr7fknmq6vi5vq6pau.apps.googleusercontent.com";
    String SCOPE = "https://www.googleapis.com/auth/tasks";
    String REDIRECT_URI = "http://localhost";

    String API_KEY = "AIzaSyCQbRE8VDPQgvmgqvy_FHxDL1cV8HAD5iQ";
    String CLIENT_SECRET = "";

    String ENDPOINT_URL = "https://www.googleapis.com/tasks/v1/users/@me/lists";

    String oauthUrl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_login);

        Button btnOauth = (Button) findViewById(R.id.testLogin);
        final WebView wvOauth = (WebView) findViewById(R.id.testWebView);

        btnOauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wvOauth.getSettings().setJavaScriptEnabled(true);
                wvOauth.setVisibility(View.VISIBLE);

                oauthUrl = new GoogleAuthorizationRequestUrl(CLIENT_ID, REDIRECT_URI, SCOPE).build();
                Log.i("Oauth", "oauthurl " + oauthUrl);

                wvOauth.setWebViewClient(new WebViewClient() {
                    public void onPageFinished(WebView view, final String url) {
                        super.onPageFinished(view, url);
                        Log.i("Oauth", "On page finished url "+ url.toString());

                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                JacksonFactory jsonFactory = new JacksonFactory();
                                HttpTransport transport = new NetHttpTransport();
                                String code = url.substring(REDIRECT_URI.length() + 7, url.length());
                                Log.i("Oauth", "Code "+ code.toString());
                                AccessTokenResponse accessTokenResponse;

                                try{
                                    accessTokenResponse = new GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant(transport,
                                            jsonFactory, CLIENT_ID, CLIENT_SECRET, code, REDIRECT_URI).execute();

                                    GoogleAccessProtectedResource accessProtectedResource =
                                            new GoogleAccessProtectedResource(accessTokenResponse.accessToken,
                                                    transport, jsonFactory, CLIENT_ID, CLIENT_SECRET, accessTokenResponse.refreshToken);

                                    com.google.api.client.http.HttpRequestFactory rf = transport.createRequestFactory(accessProtectedResource);
                                    GenericUrl endPoint = new GenericUrl(ENDPOINT_URL);
                                    try{
                                        com.google.api.client.http.HttpRequest request = rf.buildGetRequest(endPoint);
                                        com.google.api.client.http.HttpResponse response = request.execute();
                                        String str = response.parseAsString();
                                        //wvOauth.setVisibility(View.INVISIBLE);
                                    }
                                    catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                                catch(IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }

                });
                wvOauth.loadUrl(oauthUrl);
            }
        });
    }





}
