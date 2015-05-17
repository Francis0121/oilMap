package com.oilMap.client.bluetooth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis on 2015-05-17.
 */
public class DrivePointAsyncTask extends AsyncTask<Object, Void, Map<String, Object>>{

    private static final String TAG = "DrivePointAsyncTask";

    private Context mContext;

    public DrivePointAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        if(params[0] == null){
            return null;
        }

        try {
            Map<String, Object> request = new HashMap<>();
            String url = mContext.getString(R.string.contextPath) + "/drive/drivePoint";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> messages = responseEntity.getBody();
            return messages;
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            throw new RuntimeException("Driving async task communication error occur");
        }
    }

    @Override
    protected void onPostExecute(Map<String, Object> response) {
        Log.d(TAG, response.toString());
        if((Boolean)response.get("result")){
            Log.d(TAG, "Do post success");
        }
    }
}
