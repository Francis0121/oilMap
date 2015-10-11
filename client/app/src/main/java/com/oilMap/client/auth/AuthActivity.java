package com.oilMap.client.auth;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.oilMap.client.R;
import com.oilMap.client.info.OilInfoActivity_;
import com.oilMap.client.rest.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.rest.RestService;

import java.util.HashMap;
import java.util.Map;

/**
 * The TokenInfoActivity is a simple app that allows users to acquire, inspect and invalidate
 * authentication tokens for a different accounts and scopes.
 *
 * In addition see implementations of {@link AbstractGetNameTask} for an illustration of how to use
 * the {@link GoogleAuthUtil}.
 */
@Fullscreen
@EActivity(R.layout.activity_loading)
public class AuthActivity extends Activity {

    @RestService
    AARestProtocol aaRestProtocol;

    private static final String TAG = AuthActivity_.class.getSimpleName();
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;

    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getUsername();
    }

    //아래코드는 선택된 계정을 선택하여 콜백하는 과정
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You must pick an account", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if ((requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR ||
                requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
                && resultCode == RESULT_OK) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
            show("Unknown error, click the button again");
            return;
        }
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Retrying");
            getTask(this, mEmail, SCOPE).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            show("Auth rejected authorization.");
            return;
        }
        show("Unknown error, click the button again");
    }

    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (isDeviceOnline()) {
                getTask(AuthActivity.this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(this, "No network connection available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Starts an activity in Google Play Services so the user can pick an account */
    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    /** Checks whether the device currently has a network connection */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * This method is a hook for background threads and async tasks that need to update the UI.
     * It does this by launching a runnable under the UI thread.
     */
    public void show(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mOut.setText(message);
            }
        });
    }

    public void next(final Auth auth) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // ~ SharedPreference Register
                SharedPreferences pref = getSharedPreferences("userInfo", 0);
                SharedPreferences.Editor prefEdit = pref.edit();
                prefEdit.putString("id", auth.getId());
                prefEdit.commit();

                auth.setEmail(mEmail);
                requestAuthInfo(auth);
            }
        });
    }

    @Background
    void requestAuthInfo(Auth auth){
        // ~ Register Server auth information. And move activity
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        Map<String, Object> response = aaRestProtocol.authIsExistUrl(request);

        // ~ If server not have id, app add id to serer.
        if(!(Boolean)response.get("result")){
            request.put("email", auth.getEmail());
            request.put("name", auth.getName());
            response = aaRestProtocol.authInsertUrl(request);
        }
        response.put("auth", auth);
        intentActivity(response);
    }

    @Background
    void intentActivity(Map<String, Object> response){
        Intent intent = new Intent(this, OilInfoActivity_.class);
        intent.putExtra("auth", (Auth)response.get("auth"));
        startActivity(intent);
        finish();
    }

    /**
     * This method is a hook for background threads and async tasks that need to provide the
     * user a response UI when an exception occurs.
     */
    public void handleException(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            AuthActivity.this,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }

    private AbstractGetNameTask getTask(
            AuthActivity activity, String email, String scope) {
        return new GetNameInForeground(activity, email, scope);
    }

}