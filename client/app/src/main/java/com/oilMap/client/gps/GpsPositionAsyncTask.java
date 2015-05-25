package com.oilMap.client.gps;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by Francis on 2015-05-25.
 */
public class GpsPositionAsyncTask extends AsyncTask<GpsPosition, Void, Map<String,Object>> {

    private static final String TAG = "GpsPositionAsyncTask";

    private Context mContext;

    public GpsPositionAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Map<String, Object> doInBackground(GpsPosition... params) {
        if(params[0] == null){
            return null;
        }
        String url = mContext.getString(R.string.contextPath) + "/drive/gpsPosition";

        GpsPosition gpsPosition = params[0];
        Boolean isSuccess = false;
        Map<String, Object> response =  null;
        try {
            while (!isSuccess) {
                try {
                    response = postTemplate(url, gpsPosition);
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

    private Map<String, Object> postTemplate(String url, GpsPosition gpsPosition){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, gpsPosition, Map.class);
        Map<String, Object> messages = responseEntity.getBody();
        return messages;
    }

    @Override
    protected void onPostExecute(Map<String, Object> stringObjectMap) {
        super.onPostExecute(stringObjectMap);
        Log.d(TAG, stringObjectMap.toString());
    }
}
