package com.oilMap.client.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.oilMap.client.MainActivity;
import com.oilMap.client.R;
import com.oilMap.client.bluetooth.Bluetooth_reception;
import com.oilMap.client.gps.GpsActivity;
import com.oilMap.client.util.BackPressCloseHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class OilInfoActivity extends Activity {

    private static final String TAG = "OilInfoActivity";

    private BottomSheet bottomSheet;
    private BackPressCloseHandler backPressCloseHandler;
    private CircleProgress circleProgress;
    private ListView listView;
    private String id;

    private TextView dateTextView;
    private TextView moneyTextView;

    private TimerTask mTask;
    private Timer mTimer;
    private TimerTask mProgressTask;
    private Timer mProgressTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.activity_oil_info);

        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        this.id = pref.getString("id", "");

        ImageButton rankBtn = (ImageButton) findViewById(R.id.rankingBtn);
        rankBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent ranking = new Intent(OilInfoActivity.this, RankingActivity.class);
                startActivity(ranking);
            }
        });

        // ~ BottomSheet
        bottomSheet = new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog).title("Option").sheet(R.menu.list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case R.id.bluetooth:
                        Intent blue_exe = new Intent(OilInfoActivity.this, Bluetooth_reception.class);
                        blue_exe.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(blue_exe);
                        break;

                    case R.id.logout:
                        SharedPreferences pref = OilInfoActivity.this.getSharedPreferences("userInfo", 0);
                        SharedPreferences.Editor prefEdit = pref.edit();
                        prefEdit.putString("id","");
                        prefEdit.commit();

                        Intent idCheck = new Intent(OilInfoActivity.this, MainActivity.class);
                        startActivity(idCheck);
                        OilInfoActivity.this.finish();
                        break;

                    case R.id.inputfuel:
                        Intent inputFuel = new Intent(OilInfoActivity.this, InputFuelActivity.class);
                        startActivity(inputFuel);
                        break;

                    case R.id.googleMap:
                        Intent gpsIntent = new Intent(OilInfoActivity.this, GpsActivity.class);
                        startActivity(gpsIntent);
                        break;
                }
            }
        }).build();

        this.backPressCloseHandler = new BackPressCloseHandler(this);

        dateTextView = (TextView) findViewById(R.id.textView19);
        moneyTextView = (TextView) findViewById(R.id.textView22);

        new OilInfoAsyncTask().execute();

        listView = (ListView) findViewById(R.id.listView);

        // ~ Oil Visibilty
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOil();
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 100, 100);
        setSharedPreference("0", "1");
    }


    private void runOil() {

        SharedPreferences socket = getSharedPreferences("socket", 0);
        String status = socket.getString("status", "0");
        String imageType = socket.getString("imageType", "1");

        final GifImageView gifImageView = (GifImageView) findViewById(R.id.oilInfoCarGif);

        if (gifImageView.getVisibility() == View.VISIBLE) {
            if (status.equals("0")) {
                Log.d(TAG, "GONE");
                gifImageView.setVisibility(View.GONE);
            }else if(status.equals("2")){
                if(!imageType.equals("2")) {
                    Log.d(TAG, "FAST_VISIBLE");
                    gifImageView.setImageResource(R.drawable.large_ac);
                    setSharedPreference("2", "2");

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            setSharedPreference("1", "2");
                        }
                    };

                    Handler handler = new Handler();
                    handler.postDelayed(runnable, 5000);
                }
            }else if(status.equals("1")){
                if(!imageType.equals("1")) {
                    Log.d(TAG, "NORMAL_VISIBLE");
                    gifImageView.setVisibility(View.VISIBLE);
                    gifImageView.setImageResource(R.drawable.normal_ac);
                    setSharedPreference("1", "1");
                }
            }
        } else {
            if (status.equals("1")) {
                if(!imageType.equals("1")) {
                    Log.d(TAG, "NORMAL_VISIBLE");
                    gifImageView.setVisibility(View.VISIBLE);
                    gifImageView.setImageResource(R.drawable.normal_ac);
                    setSharedPreference("1", "1");
                }
            }
        }
    }

    private void setSharedPreference(String status, String imageType){
        Log.d(TAG, "setSharedPreference status " + status + " " + imageType);
        SharedPreferences pref = getSharedPreferences("socket", 0);
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putString("status", status);
        if(imageType != null) {
            prefEdit.putString("imageType", imageType);
        }
        prefEdit.commit();
    }



    @Override
    protected void onResume() {
        super.onResume();
        new OilInfoAsyncTask().execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new OilInfoAsyncTask().execute();
    }

    @Override
    public void onBackPressed() {
        this.backPressCloseHandler.onBackPressed(this.bottomSheet);
    }

    @Override
    protected void onDestroy() {
        setSharedPreference("0", "1");
        mTimer.cancel();
        if(mProgressTimer != null){
            mProgressTimer.cancel();
        }
        super.onDestroy();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }
        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View view =super.getView(position, convertView, parent);

            TextView textView=(TextView) view.findViewById(android.R.id.text1);
            /*YOUR CHOICE OF COLOR*/
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(12);
            return view;
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private class OilInfoAsyncTask extends AsyncTask<Void, Void, Map<String, Object>>{

        private Map<String, Object> request = new HashMap<String, Object>();

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            String url = getString(R.string.contextPath) + "/fuelBill/select";
            request.put("id", OilInfoActivity.this.id);

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
            }catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                response.put("result", false);
            }
            return response;
        }

        private Map<String, Object> postTemplate(String url, Map<String, Object> request){
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> messages = responseEntity.getBody();
            return messages;
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            Log.d(TAG, stringObjectMap.toString());

            if((Boolean)stringObjectMap.get("result")){
                circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
                Map<String, Object> map = (Map<String, Object>) stringObjectMap.get("fuelBill");
                Integer bill = 100;
                if(map == null || map.get("pn") == null){
                    dateTextView.setText("Data doesn't exist.");
                    moneyTextView.setText("Data doesn't exist.");
                    circleProgress.setMax(bill);
                    circleProgress.setProgress(bill);
                }else{
                    String date = ((String) map.get("billDate")).substring(0, 16);
                    bill = (Integer) map.get("bill");
                    DecimalFormat df = new DecimalFormat("#,##0");
                    String strBill = df.format(bill);

                    dateTextView.setText(date);
                    moneyTextView.setText(strBill+"￦");
                    circleProgress.setMax(bill);
                }
                Double avgGasoline = (Double) stringObjectMap.get("avgGasoline");

                List<Map<String, Object>> drivingListMap = (List<Map<String, Object>>) stringObjectMap.get("drivingList");
                List<String> list = new ArrayList<String>();

                Double totalCash = 0.0;
                if(drivingListMap.size() > 1) {
                    for (int i = 0; i < drivingListMap.size() - 1; i++) {
                        Map<String, Object> drivingMapBefore = drivingListMap.get(i);
                        Map<String, Object> drivingMapEnd = drivingListMap.get(i + 1);

                        Double calculate = (Double) drivingMapBefore.get("fuelQuantity") - (Double) drivingMapEnd.get("fuelQuantity");
                        Double distance = ((Double) drivingMapEnd.get("distance") - (Double) drivingMapBefore.get("distance"));
                        Double cash = (calculate * avgGasoline);
                        DecimalFormat df = new DecimalFormat("#,##0");
                        String strCash = df.format(cash);

                        Double efficiency = + distance / calculate;
                        DecimalFormat df2 = new DecimalFormat("#,##0.0");
                        String strEfficiency = df2.format(efficiency);

                        if(cash > 0.0) {
                            list.add("Date : " + ((String) drivingMapEnd.get("inputDate")).substring(0, 10) + " - Cash : " + strCash + "￦ - Efficiency :" + strEfficiency + "km/l");
                            totalCash += cash;
                        }
                    }
                }else{
                    list.add("Data doesn't exist");
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(OilInfoActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

                final int cash = bill- totalCash.intValue();
                //circleProgress.setProgress(cash);
                Log.d(TAG, "CASH " +cash);
                circleProgress.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(final View v) {

                        circleAnimation(cash);
                        new OilInfoAsyncTask().execute();
                    }
                });
                circleAnimation(cash);
            }
        }
    }

    private void circleAnimation(final int cash){
        if(mProgressTimer != null) {
            mProgressTimer.cancel();
        }

        circleProgress.setProgress(0);
        // ~ Oil Visibilty
        mProgressTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int value = circleProgress.getProgress()+(cash/15);

                        if(value >= cash){
                            value = cash;
                            mProgressTimer.cancel();
                        }

                        circleProgress.setProgress(value);
                    }
                });
            }
        };

        mProgressTimer = new Timer();
        mProgressTimer.schedule(mProgressTask, 200, 200);
    }
}
