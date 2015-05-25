package com.oilMap.client.bluetooth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
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
        Map<String, Object> map = new HashMap<>();
        if(params[0] == null || params[1] == null || params[2] == null || params[3] == null || params[4] == null){
            map.put("result", false);
            return map;
        }
        if(mContext == null){
            map.put("result", false);
            return map;
        }

        SharedPreferences pref = mContext.getSharedPreferences("userInfo", 0);
        String id = pref.getString("id", "");

        Double latitude = (Double) params[0];
        Double longitude = (Double) params[1];
        Double rpmNow = (Double) params[2];
        Double rpmLast = (Double) params[3];
        Integer type = (Integer) params[4];

        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        request.put("latitude", latitude);
        request.put("longitude", longitude);
        request.put("startSpeed", rpmNow);
        request.put("endSpeed", rpmLast);
        request.put("type", type);
        String url = mContext.getString(R.string.contextPath) + "/drive/drivePoint";

        Boolean isSuccess = false;
        Map<String, Object> response =  null;
        try {
            while (!isSuccess) {
                try {
                    response = postTemplate(url, request);
                    if ((Boolean) response.get("result")) {
                        isSuccess = true;
                    }
                } catch (ResourceAccessException e) {
                    Log.e("Error", e.getMessage(), e);
                    isSuccess = false;
                }
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage(), e);
            response.put("result", false);
        }

        return  response;
    }

    private Map<String, Object> postTemplate(String url, Map<String, Object> request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
        Map<String, Object> messages = responseEntity.getBody();
        return messages;
    }

    @Override
    protected void onPostExecute(Map<String, Object> response) {
        Log.d(TAG, response.toString());
        if((Boolean)response.get("result")){
            Log.d(TAG, "Do post success");
        }
    }
}
