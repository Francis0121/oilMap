package com.oilMap.client.gps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.widget.Toast;

/**
 * Created by 정성진 on 2015-03-25.
 */
public class GpsMain extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Use the LocationManager class to obtain GPS locations
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener mlocListener = new MyLocationListener(); //리스너 정의

        //LocationManager에 리스너 등록
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener); //네트워크 위치 제공자
    }

    //Class MyLocationListener
    public class MyLocationListener implements LocationListener {

        double lat, lon, speed;

        @Override
        public void onLocationChanged(Location loc) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            speed = loc.getSpeed();
            String Text = "Latitude = " + lat + "\nLongitude = " + lon + "\nSpeed = " + speed;

            Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_LONG).show();
        }
    }
}
