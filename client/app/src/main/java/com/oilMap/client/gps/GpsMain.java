package com.oilMap.client.gps;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import com.oilMap.client.R;

/**
 * Created by 정성진 on 2015-03-25.
 */
public class GpsMain extends Activity {

    Button GpsStartButton;
    Button GpsFinishButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_main);

        //Use the LocationManager class to obtain GPS locations
        final LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        final LocationListener mlocListener = new MyLocationListener(); //리스너 정의

        //버튼 정의
        GpsStartButton = (Button)findViewById(R.id.GpsStartButton);
        GpsFinishButton = (Button)findViewById(R.id.GpsFinishButton);

        //START 버튼 눌렀을 때 gps 신호 받기 시작
        GpsStartButton.setOnClickListener(new View.OnClickListener() { //START 버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "waiting...", Toast.LENGTH_SHORT).show();

                //LocationManager에 리스너 등록
                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener); //네트워크 위치 제공자
            }
        });

        //FINISH 버튼 누르면 gps 수신 종료
        GpsFinishButton.setOnClickListener(new View.OnClickListener() { //FINISH 버튼 눌렀을 때

            @Override
            public void onClick(View v) {
                mlocManager.removeUpdates(mlocListener);
            }
        });
    }


    //Class MyLocationListener
    public class MyLocationListener implements LocationListener {

        //Slat : Start Latitude, Slon : Start Longitude, Elat : End Latitude, Elon : End Longitude
        double Slat=0.0, Slon=0.0, Elat=0.0, Elon=0.0, speed=0.0, distance=0.0;

        @Override
        public void onLocationChanged(Location loc) {

            Location location1 = new Location("1"), location2 = new Location("2");
            Slat = Elat;
            Slon = Elon;
            Elat = loc.getLatitude();
            Elon = loc.getLongitude();

            //location1에 이전 latitude, longitude 로설정.
            //location2에 현재 latitude, longitude 로설정.
            location1.setLatitude(Slat);
            location1.setLongitude(Slon);
            location2.setLatitude(Elat);
            location2.setLongitude(Elon);

            speed = loc.getSpeed();

            if(Slat != 0.0) { //Slat, Slon 가 처음에 0.0으로 되어있으므로
                distance += location1.distanceTo(location2); //거리 계산

                String Text = "Speed = " + speed + "\nDistance = " + distance + "m";

                Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
            }

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