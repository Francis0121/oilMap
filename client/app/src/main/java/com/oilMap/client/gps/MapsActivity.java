package com.oilMap.client.gps;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.oilMap.client.R;

public class MapsActivity extends FragmentActivity{

    private static final String TAG = "MapsActivity";

    private GoogleMap mGoogleMap;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.activity_full_map);

        Intent config = getIntent();
        this.id = config.getExtras().getString("id");         //아이디값을 받아옴

        MapsInitializer.initialize(getApplicationContext());

        init();
    }

    private void init() {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapsActivity.this);
        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Gpsinfo gps = new Gpsinfo(MapsActivity.this);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);// Creating a LatLng object for the current location
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));// Showing the current location in Google Map
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            new MapsAsyncTask(MapsActivity.this, mGoogleMap).execute(id);
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        }
    }

    private Boolean isFirstTime = true;

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d(TAG, "Call on my location change listener");
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            new MapsOtherAsyncTask(MapsActivity.this, mGoogleMap, id).execute(loc);
            if(mGoogleMap != null){
                if(isFirstTime) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                    isFirstTime = false;
                }
            }
        }
    };
}