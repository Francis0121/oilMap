package com.oilMap.client.map;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-17.
 */
public class MapsAsyncTask extends AsyncTask<String, Void, Map<String,Object>> {

    private static final String TAG = "MapsAsyncTask";

    private Context context;
    private GoogleMap googleMap;

    public MapsAsyncTask(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
    }

    @Override
    protected Map<String, Object> doInBackground(String... params) {
        if(params[0] ==null){
            return null;
        }
        Map<String, Object> request = new HashMap<>();
        request.put("id", params[0]);
        Log.d(TAG, request.toString());

        String url = context.getString(R.string.contextPath) + "/drive/position";

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

            response = new HashMap<>();
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

        List<Map<String, Object>> drivePointListMap = (List<Map<String, Object>>) response.get("drivePointList");
        if(drivePointListMap == null) {
            return;
        }
        Log.d(TAG, drivePointListMap.toString());

        int count = 0;
        for(Map<String, Object> drivePointMap : drivePointListMap){
            count++;
            Double latitude = (Double) drivePointMap.get("latitude");
            Double longitude = (Double) drivePointMap.get("longitude");
            Double startSpeed = (Double) drivePointMap.get("startSpeed");
            Double endSpeed = (Double) drivePointMap.get("endSpeed");
            Integer type = (Integer) drivePointMap.get("type");

            DecimalFormat df = new DecimalFormat("#,##0.0");
            String strSpeed = df.format( (endSpeed - startSpeed));

           LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions option = new MarkerOptions();
            option.position(latLng);
            option.title("Position");
            option.snippet("Diff RPM " +  strSpeed);
            switch (type){
                case 0:
                    option.icon(BitmapDescriptorFactory.fromResource(R.drawable.position));
                    break;
                case 1:
                    option.icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_position));
                    break;
            }

            this.googleMap.addMarker(option).showInfoWindow();
            Log.d(TAG, latLng.toString());
        }
    }

}
