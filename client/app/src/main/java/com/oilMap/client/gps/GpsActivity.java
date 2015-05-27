package com.oilMap.client.gps;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.oilMap.client.R;
import com.oilMap.client.bluetooth.DrivePointAsyncTask;

import java.text.DateFormat;
import java.util.Date;

public class GpsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "GpsActivity";
    private String id = "";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.activity_gps);

        MapsInitializer.initialize(getApplicationContext());
        init();
        buildGoogleApiClient();
    }

    private void init() {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(GpsActivity.this);
        mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activityGpsMap)).getMap();

        GpsInfo gps = new GpsInfo(GpsActivity.this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);// Creating a LatLng object for the current location
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));// Showing the current location in Google Map
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mGoogleMap.setMyLocationEnabled(true);
        }
        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        id = pref.getString("id", "");

        ImageButton imageBtn = (ImageButton) findViewById(R.id.gpsImageButton);
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GpsSelectDrivingAsyncTask(GpsActivity.this, GpsActivity.this.mGoogleMap).execute(id);
            }
        });
    }

    // ~ Location Request

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private PolylineOptions mPolylineOptions;
    private Location mBeforeLocation;
    private Double beforeSpeed;
    private Double currentSpeed;

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI();
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mBeforeLocation = mCurrentLocation;
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "Latitude " + String.valueOf(mCurrentLocation.getLatitude()) + "Longitude " + String.valueOf(mCurrentLocation.getLongitude())+ " " + mLastUpdateTime);
        if(mGoogleMap != null){
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            if(mPolylineOptions == null) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20.0f));
                mPolylineOptions = new PolylineOptions().add(latLng);
                mPolylineOptions.color(Color.rgb(241,140,36));
            }else{
                mPolylineOptions.add(latLng);
            }
            mGoogleMap.clear();
            Polyline polyline = mGoogleMap.addPolyline(mPolylineOptions);
        }

        if(mBeforeLocation != null && mCurrentLocation != null){
            Double distance = DistanceCalculator.distance(mBeforeLocation, mCurrentLocation);
            if(distance.compareTo(0.0) > 0){
                Log.d(TAG, "Location move " + distance.toString() + " m ");
                Double speed = distance / (UPDATE_INTERVAL_IN_MILLISECONDS/1000);
                Log.d(TAG, "Speed " + speed.toString() + " m/s");
                if(speed.compareTo(0.0) > 0){
                    TextView textView = (TextView) findViewById(R.id.gpsSpeedTextView);
                    textView.setText(""+speed.intValue());
                    if(currentSpeed != null) {
                        beforeSpeed = currentSpeed;
                    }
                    currentSpeed = speed;
                }

                if( (beforeSpeed != null && beforeSpeed.compareTo(5.0) > 0) && (currentSpeed != null && currentSpeed.compareTo(5.0) > 0)){
                    Double diffSpeed = currentSpeed - beforeSpeed;
                    if(diffSpeed.compareTo(10.0) > 0) {
                        // Acceleator position
                        new DrivePointAsyncTask(GpsActivity.this).execute(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), beforeSpeed, currentSpeed, 1);
                    }
                }

                new GpsPositionAsyncTask(GpsActivity.this).execute(new GpsPosition(id, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

}
