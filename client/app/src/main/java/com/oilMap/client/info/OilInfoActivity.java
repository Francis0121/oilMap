package com.oilMap.client.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.oilMap.client.MainActivity;
import com.oilMap.client.R;
import com.oilMap.client.bluetooth.Bluetooth_reception;
import com.oilMap.client.user.SMSReceiver;
import com.oilMap.client.util.BackPressCloseHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OilInfoActivity extends Activity {

    private static final String TAG = "OilInfoActivity";

    private BottomSheet bottomSheet;
    private BackPressCloseHandler backPressCloseHandler;
    private CircleProgress circleProgress;
    private ListView listView;
    private String id;

    private TextView dateTextView;
    private TextView moneyTextView;

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
                        OilInfoActivity.this.finish();
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
                }
            }
        }).build();

        this.backPressCloseHandler = new BackPressCloseHandler(this);

        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        circleProgress.setProgress(10);
        circleProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleProgress.setProgress(80);
            }
        });


        dateTextView = (TextView) findViewById(R.id.textView19);
        moneyTextView = (TextView) findViewById(R.id.textView22);

        new OilInfoAsyncTask().execute();

        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onBackPressed() {
        this.backPressCloseHandler.onBackPressed(this.bottomSheet);
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
            try {
                String url = getString(R.string.contextPath) + "/fuelBill/select";
                request.put("id", OilInfoActivity.this.id);

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, this.request, Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;
            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                throw new RuntimeException("Communication error occur");
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            Log.d(TAG, stringObjectMap.toString());

            if((Boolean)stringObjectMap.get("result")){
                Map<String, Object> map = (Map<String, Object>) stringObjectMap.get("fuelBill");
                if(map == null || map.get("pn") == null){
                    dateTextView.setText("Data doesn't exist.");
                    moneyTextView.setText("Data doesn't exist.");
                }else{
                    String date = ((String) map.get("billDate")).substring(0, 16);
                    Integer bill = (Integer) map.get("bill");
                    DecimalFormat df = new DecimalFormat("#,##0");
                    String strBill = df.format(bill);

                    dateTextView.setText(date);
                    moneyTextView.setText(strBill);
                }
                Double avgGasoline = (Double) stringObjectMap.get("avgGasoline");

                List<Map<String, Object>> drivingListMap = (List<Map<String, Object>>) stringObjectMap.get("drivingList");
                List<String> list = new ArrayList<String>();
                if(drivingListMap.size() > 1) {
                    for (int i = 0; i < drivingListMap.size() - 1; i++) {
                        Map<String, Object> drivingMapBefore = drivingListMap.get(i);
                        Map<String, Object> drivingMapEnd = drivingListMap.get(i + 1);

                        Double calculate = (Double) drivingMapBefore.get("fuelQuantity") - (Double) drivingMapEnd.get("fuelQuantity");
                        Double distance = ((Double) drivingMapEnd.get("distance") - (Double) drivingMapBefore.get("distance"));
                        Double cash = (calculate * avgGasoline);
                        DecimalFormat df = new DecimalFormat("#,##0");
                        String strCash = df.format(cash);

                        list.add("Date : " + ((String) drivingMapEnd.get("inputDate")).substring(0, 10) + " - Cash : " + strCash + "￦ - Efficiency :" + distance / calculate + "km/l");
                    }
                }else{
                    list.add("Data doesn't exist");
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(OilInfoActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

            }

        }
    }
}
