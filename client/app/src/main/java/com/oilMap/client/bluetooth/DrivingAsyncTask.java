package com.oilMap.client.bluetooth;

import android.content.Context;
import android.content.SharedPreferences;
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
public class DrivingAsyncTask  extends AsyncTask<Object, Void, Map<String, Object>>{

    private static final String TAG = "DrivingPointAsyncTask";

    private Context mContext;

    public DrivingAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        Map<String, Object> map = new HashMap<>();
        if(params[0] == null || params[1] == null){
            map.put("result", false);
            return map;
        }

        try {
            SharedPreferences pref = mContext.getSharedPreferences("userInfo", 0);
            String id = pref.getString("id", "");
            Double distance = (Double) params[0];
            Double fuelQuantity = (Double) params[1];

            Map<String, Object> request = new HashMap<>();
            request.put("distance", distance);
            request.put("fuelQuantity", fuelQuantity);
            request.put("id", id);
            Log.d(TAG, request.toString());

            String url = mContext.getString(R.string.contextPath) + "/drive/driving";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> messages = responseEntity.getBody();
            return messages;
        } catch (Exception e) {
            Log.e("Error", e.getMessage(), e);
            map.put("result", false);
            return map;
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
