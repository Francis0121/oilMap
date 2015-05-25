package com.oilMap.client.gps;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-25.
 */
public class GpsSelectDrivingAsyncTask extends AsyncTask<String, Void, GpsResponse> {

    private static final String TAG = "GpsSelectDrivingAyncTask";

    private Context mContext;
    private GoogleMap mGoogleMap;

    public GpsSelectDrivingAsyncTask(Context mContext, GoogleMap mGoogleMap) {
        this.mGoogleMap = mGoogleMap;
        this.mContext = mContext;
    }

    @Override
    protected GpsResponse doInBackground(String... params) {
        if(params[0] == null){
            return null;
        }
        String url = mContext.getString(R.string.contextPath) + "/drive/gpsPosition/select";
        String id = params[0];
        Map<String, Object> request = new HashMap<>();
        request.put("id", id);

        Boolean isSuccess = false;
        GpsResponse response =  null;
        try {
            while (!isSuccess) {
                try {
                    response = postTemplate(url, request);
                    if (response.getResult()) {
                        isSuccess = true;
                    }
                } catch (ResourceAccessException e) {
                    Log.e("Error", e.getMessage(), e);
                    isSuccess = false;
                }
            }
        }catch (Exception e){
            Log.e("Error", e.getMessage(), e);
            response = new GpsResponse();
            response.setResult(false);
        }

        return response;
    }

    private GpsResponse postTemplate(String url, Map<String, Object> request){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GpsResponse> responseEntity = restTemplate.postForEntity(url, request, GpsResponse.class);
        return responseEntity.getBody();
    }



    @Override
    protected void onPostExecute(GpsResponse gpsResponse) {
        super.onPostExecute(gpsResponse);
        Log.d(TAG,""+gpsResponse.toString());

        PolylineOptions polylineOptions = new PolylineOptions();
        List<GpsPosition> gpsPositionList = gpsResponse.getGpsPositionList();

        for(GpsPosition gpsPosition : gpsPositionList) {
            LatLng latLng = new LatLng(gpsPosition.getLatitude(), gpsPosition.getLongitude());
            polylineOptions.add(latLng);
        }
        Polyline polyline = this.mGoogleMap.addPolyline(polylineOptions);
    }
}
