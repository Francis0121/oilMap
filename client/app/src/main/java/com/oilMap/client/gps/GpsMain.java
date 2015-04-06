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

import java.text.SimpleDateFormat;

/**
 * Created by 정성진 on 2015-03-25.
 * Revised by 나홍철 on 2015-03-31.
 */
public class GpsMain extends Activity {

    Button GpsFinishButton;

    // GPS 신호를 처리하는 Location Manager
    LocationManager locManager; // 위치 매니저 생성
    LocationListener locListener; // GPS 신호 리스너 생성

    public void onCreate(Bundle savedInstanceState) {
        // 생성자
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_main);

        // Location manager
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyLocationListener();

        //버튼 정의
        GpsFinishButton = (Button)findViewById(R.id.GpsFinishButton);

        /////////////////////////////////////////////////////GPS AUTO START//////////////////////
        Toast.makeText(getApplicationContext(), "waiting...", Toast.LENGTH_SHORT).show();

        //LocationManager 에 리스너 등록
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locListener);      // GPS 신호 1000(1초) 1m

        /*
        /////////////////////////////////////////////////////GPS AUTO START for TEST//////////////////////
        Toast.makeText(getApplicationContext(), "GPS Auto Start\nfor TEST", Toast.LENGTH_SHORT).show();

        Criteria criteria = new Criteria();
        // 정확도
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 전원 소비량
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 속도
        criteria.setSpeedRequired(true);
        // 위치 정보를 얻어 오는데 들어가는 금전적 비용
        criteria.setCostAllowed(true);
        String bsetProvider = locManager.getBestProvider(criteria, true);

        //LocationManager 에 리스너 등록
        locManager.requestLocationUpdates(bsetProvider, 1000, 1, locListener);      // GPS 신호 1000(1초) 1m

        //////////////////////////////////////////////////////////////////////////////////////////
        */

        //FINISH 버튼 누르면 gps 수신 종료
        GpsFinishButton.setOnClickListener(new View.OnClickListener() { //FINISH 버튼 눌렀을 때

            @Override
            public void onClick(View v) {
                locManager.removeUpdates(locListener);
            }
        });
    }

    // 프로그램 종료시 GPS 끈다. --> 실제 프로그램에서는??
    protected void onStop() {
        locManager.removeUpdates(locListener);
        locListener = null;
        super.onStop();
    }


    //Class MyLocationListener
    public class MyLocationListener implements LocationListener {

        /*
        //sLat : Start Latitude, sLon : Start Longitude, eLat : End Latitude, eLon : End Longitude
        double sLat=0.0, sLon=0.0, eLat=0.0, eLon=0.0;
        // distance : 거리 , SumDistance : 총거리, timeInterval : 두신호간 시간차이 , lastTimeInterval : 이전신호 시간차이 저장
        double distance=0.0, sumDistance=0.0, timeInterval=0.0, lastTimeInterval=0.0;
        // spped : 현재속도, lastSpeed : 이전 속도 , acc : 가속도
        double speed=0.0 , lastSpeed=0.0 , acc=0.0;
        // lastTime 이전신호 시간, nowTime : 현재시간
        long lastTime=0, nowTime=0;
        */

        double sLat=0.0, sLon=0.0, eLat=0.0, eLon=0.0;
        double distance=0.0, sumDistance=0.0;
        double speed=0.0;
        long lastTime=0, nowTime=0;
        String txt = "\0";

        @Override
        public void onLocationChanged(Location loc) {
            // 위치 변화시 사용
            Location location1 = new Location("1"), location2 = new Location("2");
            sLat = eLat;
            sLon = eLon;
            eLat = loc.getLatitude();
            eLon = loc.getLongitude();

            lastTime = nowTime;
            nowTime = loc.getTime();

            //location1에 이전 latitude, longitude 로설정.
            //location2에 현재 latitude, longitude 로설정.
            location1.setLatitude(sLat);
            location1.setLongitude(sLon);
            location2.setLatitude(eLat);
            location2.setLongitude(eLon);

            //시작 위치가 초기화값이 아니고, 속도값이 존재할 경우
            if((sLat != 0.0) && loc.hasSpeed()) {

                speed = loc.getSpeed() * 3600/1000;

                if(speed == 0.0)
                    distance = 0.0;
                else
                    distance = location1.distanceTo(location2);

                sumDistance += distance;

                txt = "distance = " + distance +
                        "\nsumDistance = " + sumDistance +
                        "\nnowTime = " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime) +
                        "\nspeed = " + speed + "km/h";

                Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "no value!", Toast.LENGTH_SHORT).show();

            /*
            distance =location1.distanceTo(location2);
            nowTime=loc.getTime();

            // 두지점의 시간간격
            if(lastTime !=0.0)
                timeInterval=(nowTime - lastTime)/1000; // 두 위치의 시간차이를 구한다.

            if(timeInterval > 0) {
                speed = (distance / timeInterval);  // m/s

                if (sLat != 0.0) { //sㅣat, sLon 가 처음에 0.0으로 되어있으므로
                    sumDistance += distance/1000; // 총 거리 계산 단위 km

                    // (avg) a = (v2-v1)/(t2-t1) => t2-t1 은 두속도간 시간차이이므로
                    // 이전 속도에 사용된 시간차와 현재 속도의 시간차를 더하면된다.
                    acc = (speed - lastSpeed)/(lastTimeInterval + timeInterval);
                   // String Text = acc+"\n"+speed+"\n"+lastSpeed+"\n"+nowTime+"\n"+lastTime;

                    String Text = "\nTime Interval(s) = "+ timeInterval +"\nDistance(m) = " +  Double.parseDouble(String.format("%.2f",distance))
                            + "\nSpeed(km/h) = " + Double.parseDouble(String.format("%.2f",speed*(3600/1000)))
                            + "\nAcceleration(km/h) = " + Double.parseDouble(String.format("%.4f",acc*(3600/1000)))
                            + "\nSum of Distance(km) = " +  Double.parseDouble(String.format("%.2f",sumDistance));

                    Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_LONG).show();
                }
            }

            lastTime=nowTime;
            lastSpeed=speed;
            lastTimeInterval = timeInterval; // 현위치의 시간정보를 저장한다.

            */
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 프로바이더 상태가 변경됨
        }

        @Override
        public void onProviderEnabled(String provider) {
            // 프로바이더가 활성화됨
            Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // 프로바이더가 비활성화됨
            Toast.makeText(getApplicationContext(), "GPS Disabled", Toast.LENGTH_LONG).show();
        }
    }
}