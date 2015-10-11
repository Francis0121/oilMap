package com.oilMap.client.info;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.oilMap.client.R;
import com.oilMap.client.common.UserInfoPrefs_;
import com.oilMap.client.common.AARestProtocol;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Map;

// ~ Use Activity
@EActivity(R.layout.activity_input_fuel)
public class InputFuelActivity extends Activity {

    private static final String TAG = InputFuelActivity_.class.getSimpleName();

    @ViewById
    EditText inputFuelText;

    @ViewById
    TextView inputFuelErrorText;

    @Pref
    UserInfoPrefs_ userInfoPrefs;

    @RestService
    AARestProtocol aaRestProtocol;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Click
    void inputFuelBtnOk(){
        if(inputFuelText.getText() == null || inputFuelText.getText().toString().equals("")){
            inputFuelErrorText.setVisibility(View.VISIBLE);
            inputFuelText.setText("Please input fuel payment");
            return;
        }

        String id = userInfoPrefs.id().get();
        Integer price = Integer.parseInt(inputFuelText.getText().toString());

        Map<String, Object> request = new HashMap<>();
        request.put("id", id);
        request.put("bill", price);
        request.put("title", "사용자 직접 입력");
        insertFuel(request);

        OilInfoActivity_.intent(this).start();
        finish();
    }

    @Background
    void insertFuel(Map<String, Object> request){
        Map<String, Object> response = aaRestProtocol.fuelBillInsert(request);
        Log.d(TAG, response.toString());
        if((Boolean) response.get("result")){
            Log.d(TAG, "Bill Async task success");
        }
    }

}
