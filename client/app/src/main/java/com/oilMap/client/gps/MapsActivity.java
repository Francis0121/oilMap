package com.oilMap.client.gps;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map);

        MapsInitializer.initialize(getApplicationContext());

        init();

        /*this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar


        Log.d("MapsActivity", "onCreate");
        setUpMapIfNeeded(new LatLng(37.56641923090,126.9778741551));*/
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

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Map 을 zoom 합니다.
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            // 마커 설정.
            MarkerOptions optFirst = new MarkerOptions();
            optFirst.position(latLng);// 위도 • 경도
            optFirst.title("Current Position");// 제목 미리보기
            optFirst.snippet("Snippet");
            optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_icon));
            mGoogleMap.addMarker(optFirst).showInfoWindow();
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
}