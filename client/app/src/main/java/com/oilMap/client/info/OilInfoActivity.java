package com.oilMap.client.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.oilMap.client.MainActivity_;
import com.oilMap.client.R;
import com.oilMap.client.auth.Auth;
import com.oilMap.client.bluetooth.BluetoothReceptionActivity;
import com.oilMap.client.common.Bsfc;
import com.oilMap.client.common.StatusPrefs_;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.common.AARestProtocol;
import com.oilMap.client.util.BackPressCloseHandler;
import com.oilMap.client.util.ExcelUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

// ~ Use Activity
@Fullscreen
@EActivity(R.layout.activity_oil_info)
public class OilInfoActivity extends Activity {

    private static final String TAG = OilInfoActivity_.class.getSimpleName();

    @ViewById
    ListView listView;

    @ViewById
    TextView dateTextView;

    @ViewById
    TextView moneyTextView;

    @ViewById
    TextView remainTextView;

    @RestService
    AARestProtocol aaRestProtocol;

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @Pref
    StatusPrefs_ statusPrefs;

    @Extra
    Auth auth;

    private BottomSheet bottomSheet;
    private BackPressCloseHandler backPressCloseHandler;
    private String id;

    @Click
    void rankingBtn(){
        RankingActivity_.intent(this).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestFuelBillSelect();
        // ~ Bsfc Confirm UiThread
        scheduleBsfc();

        this.id = userInfoPrefs.id().get();
        changeStatus("0", "1");
        // ~ BottomSheet
        bottomSheet = new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog).title("Option").sheet(R.menu.oli_info_activity_list).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case R.id.bluetooth:
                        Intent blue_exe = new Intent(OilInfoActivity.this, BluetoothReceptionActivity.class);
                        blue_exe.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(blue_exe);
                        break;

                    case R.id.logout:
                        userInfoPrefs.id().remove();
                        MainActivity_.intent(OilInfoActivity.this).start();
                        OilInfoActivity.this.finish();
                        break;

                    case R.id.inputfuel:
                        InputFuelActivity_.intent(OilInfoActivity.this).start();
                        break;
                }
            }
        }).build();
        this.backPressCloseHandler = new BackPressCloseHandler(this);

        // ~ Read Excel
        readDataFromExcel();
        setRpmAndEngineLoadFromExcelUtil();
    }

    @UiThread(delay = 100)
    void runOil() {
        String status = statusPrefs.status().get();
        String imageType = statusPrefs.imageType().get();

        final GifImageView gifImageView = (GifImageView) findViewById(R.id.oilInfoCarGif);

        if (gifImageView.getVisibility() == View.VISIBLE) {
            if (status.equals("0")) {
                Log.d(TAG, "GONE");
                gifImageView.setVisibility(View.GONE);
            }else if(status.equals("2")){
                if(!imageType.equals("2")) {
                    Log.d(TAG, "FAST_VISIBLE");
                    gifImageView.setImageResource(R.drawable.large_ac);
                    changeStatus("2", "2");

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            changeStatus("1", "2");
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
                    changeStatus("1", "1");
                }
            }
        } else {
            if (status.equals("1")) {
                if(!imageType.equals("1")) {
                    Log.d(TAG, "NORMAL_VISIBLE");
                    gifImageView.setVisibility(View.VISIBLE);
                    gifImageView.setImageResource(R.drawable.normal_ac);
                    changeStatus("1", "1");
                }
            }
        }
        runOil();
    }

    private void changeStatus(String status, String imageType){
        Log.d(TAG, "changeStatus status " + status + " " + imageType);
        statusPrefs.status().put(status);
        if(imageType != null) {
            statusPrefs.imageType().put(imageType);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFuelBillSelect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestFuelBillSelect();
    }

    @Override
    public void onBackPressed() {
        this.backPressCloseHandler.onBackPressed(this.bottomSheet);
    }

    @Override
    protected void onDestroy() {
        changeStatus("0", "1");
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

    @Background
    void requestFuelBillSelect(){
        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        Map<String, Object> response = aaRestProtocol.fuelBillSelectUrl(request);
        responseFuelBillSelect(response);
    }

    @UiThread
    void responseFuelBillSelect(Map<String, Object> response){
        Log.d(TAG, response.toString());

        if((Boolean)response.get("result")){
            Map<String, Object> map = (Map<String, Object>) response.get("fuelBill");
            Integer bill = 100;
            if(map == null || map.get("pn") == null){
                dateTextView.setText("Data doesn't exist.");
                moneyTextView.setText("Data doesn't exist.");
                remainTextView.setText("Data doesn't exist.");

            }else{
                String date = ((String) map.get("billDate")).substring(0, 16);
                bill = (Integer) map.get("bill");
                DecimalFormat df = new DecimalFormat("#,##0");
                String strBill = df.format(bill);

                dateTextView.setText(date);
                moneyTextView.setText(strBill + "￦");
            }
            Double avgGasoline = (Double) response.get("avgGasoline");

            List<Map<String, Object>> drivingListMap = (List<Map<String, Object>>) response.get("drivingList");
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
            final StableArrayAdapter adapter = new StableArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);

            final int cash = bill- totalCash.intValue();
            Log.d(TAG, "CASH " + cash);
            DecimalFormat df = new DecimalFormat("#,##0");
            String strBill = df.format(cash);
            remainTextView.setText(strBill+"￦");
        }
    }

    // RPM, Engine Load variable and event

    @ViewById
    TextView engineLoadTextView;

    @Click
    void engineUpButton(){
        Integer engineLoad = Integer.parseInt((String) engineLoadTextView.getText());
        if(engineLoad < 100) {
            if (engineLoad == 80) {
                engineLoad = 100;
            } else if (engineLoad == 70) {
                engineLoad = 80;
            } else if (engineLoad == 60) {
                engineLoad = 70;
            } else {
                engineLoad += 5;
            }
        }
        engineLoadTextView.setText(engineLoad.toString());
    }

    @Click
    void engineDownButton(){
        Integer engineLoad = Integer.parseInt((String) engineLoadTextView.getText());
        if(engineLoad > 25){
            if(engineLoad == 100){
                engineLoad = 80;
            }else if(engineLoad == 80){
                engineLoad = 70;
            }else if(engineLoad == 70){
                engineLoad = 60;
            }else{
                engineLoad-=5;
            }
        }
        engineLoadTextView.setText(engineLoad.toString());
    }

    @ViewById
    TextView rpmTextView;

    @Click
    void rpmUpButton(){
        Integer rpm = Integer.parseInt((String) rpmTextView.getText());
        if(rpm < 3500){
            rpm+=100;
        }
        rpmTextView.setText(rpm.toString());
    }

    @Click
    void rpmDownButton(){
        Integer rpm = Integer.parseInt((String) rpmTextView.getText());
        if(rpm > 1100){
            rpm-=100;
        }
        rpmTextView.setText(rpm.toString());
    }

    @UiThread(delay = 3000)
    void scheduleBsfc(){

        Integer rpm = Integer.parseInt((String) rpmTextView.getText());
        Integer engineLoad = Integer.parseInt((String) engineLoadTextView.getText());

        requestBsfcInfo(new Bsfc(rpm, engineLoad));
        scheduleBsfc();
    }

    @Background
    void requestBsfcInfo(Bsfc bsfc){
        responseBsfcInfo(aaRestProtocol.bsfcSelectUrl(bsfc));
    }

    @ViewById
    TextView levelTextView;

    @ViewById(R.id.engineUpButton)
    ImageButton engineUpBtn;

    @ViewById(R.id.engineDownButton)
    ImageButton engineDownBtn;

    @ViewById(R.id.rpmUpButton)
    ImageButton rpmUpBtn;

    @ViewById(R.id.rpmDownButton)
    ImageButton rpmDownBtn;

    @UiThread
    void responseBsfcInfo(Bsfc bsfc){
        // TODO UPDATE RPM, ENGINE_LAOAD, LEVEL
        Log.d(TAG, bsfc.toString());
        changeLevelTextView(bsfc.getLevel());
        changeEngineLoad(bsfc.getElChange());
        changeRpm(bsfc.getRpmChange());
    }

    @ColorRes(R.color.level7)
    int level7;
    @ColorRes(R.color.level6)
    int level6;
    @ColorRes(R.color.level5)
    int level5;
    @ColorRes(R.color.level4)
    int level4;
    @ColorRes(R.color.level3)
    int level3;
    @ColorRes(R.color.level2)
    int level2;
    @ColorRes(R.color.level1)
    int level1;

    private void changeLevelTextView(Integer level){
        String text = "Level " + level;
        levelTextView.setText(text);
        switch (level){
            case 1:
                levelTextView.setBackgroundColor(level1);
                break;
            case 2:
                levelTextView.setBackgroundColor(level2);
                break;
            case 3:
                levelTextView.setBackgroundColor(level3);
                break;
            case 4:
                levelTextView.setBackgroundColor(level4);
                break;
            case 5:
                levelTextView.setBackgroundColor(level5);
                break;
            case 6:
                levelTextView.setBackgroundColor(level6);
                break;
            case 7:
                levelTextView.setBackgroundColor(level7);
                break;
        }
    }

    private void changeEngineLoad(Integer level){
        switch (level){
            case 0:
                engineDownBtn.setImageResource(R.drawable.down);
                engineUpBtn.setImageResource(R.drawable.up);
                break;
            case 1:
                engineDownBtn.setImageResource(R.drawable.down_blue);
                engineUpBtn.setImageResource(R.drawable.up_red);
                break;
            case 2:
                engineDownBtn.setImageResource(R.drawable.down_red);
                engineUpBtn.setImageResource(R.drawable.up_blue);
                break;
        }
    }

    private void changeRpm(Integer level){
        switch (level){
            case 0:
                rpmDownBtn.setImageResource(R.drawable.down);
                rpmUpBtn.setImageResource(R.drawable.up);
                break;
            case 1:
                rpmDownBtn.setImageResource(R.drawable.down_blue);
                rpmUpBtn.setImageResource(R.drawable.up_red);
                break;
            case 2:
                rpmDownBtn.setImageResource(R.drawable.down_red);
                rpmUpBtn.setImageResource(R.drawable.up_blue);
                break;
        }
    }

    // ~ ReadExcel

    @Background
    public void readDataFromExcel(){
        ExcelUtil.readDataFromExcel();
    }

    @UiThread(delay = 2500)
    public void setRpmAndEngineLoadFromExcelUtil(){
        rpmTextView.setText(Integer.toString(ExcelUtil.rpm));
        engineLoadTextView.setText(Integer.toString(ExcelUtil.engineLoad));
        Log.d(TAG, "setRpmAndEngineLoadFromExcelUtil" +Integer.toString(ExcelUtil.rpm) + " " + Integer.toString(ExcelUtil.engineLoad));

        setRpmAndEngineLoadFromExcelUtil();
    }
}
