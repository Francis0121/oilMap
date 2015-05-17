package com.oilMap.client.gps;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-17.
 */
public class MapsAsyncTask extends AsyncTask<Void, Void, Map<String,Object>> {

    private static final String TAG = "MapsAsyncTask";

    private Map<Marker, Map<String, Object>> markerHashMap= new HashMap<>();
    private Context context;
    private GoogleMap googleMap;

    public MapsAsyncTask(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
    }

    @Override
    protected Map<String, Object> doInBackground(Void... params) {

        try {
            SharedPreferences pref = context.getSharedPreferences("userInfo", 0);
            String id = pref.getString("id", "");

            Map<String, Object> request = new HashMap<>();
            request.put("id", id);
            Log.d(TAG, request.toString());

            String url = context.getString(R.string.contextPath) + "/drive/position";
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

        List<Map<String, Object>> drivePointListMap = (List<Map<String, Object>>) response.get("drivePointList");
        Log.d(TAG, drivePointListMap.toString());

        int count = 0;
        for(Map<String, Object> drivePointMap : drivePointListMap){
            count++;
            Double latitude = (Double) drivePointMap.get("latitude");
            Double longitude = (Double) drivePointMap.get("longitude");

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions option = new MarkerOptions();
            option.position(latLng);
            option.title("Position");// 제목 미리보기
            option.snippet("Speed " + count);
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.position));
            this.googleMap.addMarker(option).showInfoWindow();
            Log.d(TAG, latLng.toString());
        }
    }

}
