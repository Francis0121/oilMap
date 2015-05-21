package com.oilMap.client.gps;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oilMap.client.R;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener{

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

        // 맵의 이동
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

        GpsInfo gps = new GpsInfo(MapsActivity.this);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setUpMapIfNeeded(LatLng latLng) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGoogleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            FragmentManager fm = getFragmentManager();
            mGoogleMap = ((MapFragment) fm.findFragmentById(R.id.map)).getMap();
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        // 현재 위도와 경도에서 화면 포인트를 알려준다
        Point screenPt = mGoogleMap.getProjection().toScreenLocation(point);

        // 현재 화면에 찍힌 포인트로 부터 위도와 경도를 알려준다.
        LatLng latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);

        Log.d("맵좌표", "좌표: 위도(" + String.valueOf(point.latitude) + "), 경도("
                + String.valueOf(point.longitude) + ")");
        Log.d("화면좌표", "화면좌표: X(" + String.valueOf(screenPt.x) + "), Y("
                + String.valueOf(screenPt.y) + ")");
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d(TAG, "Call on my location change listener");
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions option = new MarkerOptions();
            option.position(loc);// 위도 • 경도
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.curr_position));

            new MapsOtherAsyncTask(MapsActivity.this, mGoogleMap, id).execute(loc);
//            mGoogleMap.addMarker(option);
            if(mGoogleMap != null){
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };
}