package com.oilMap.client.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oilMap.client.R;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// ~ Use Activity
public class InputFuelActivity extends Activity {

    private static final String TAG = "InputFuelActivity";

    private EditText inputFuelText;
    private TextView inputFuelErrorText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
        setContentView(R.layout.input_fuel);

        inputFuelText = (EditText) findViewById(R.id.inputFuelText);
        inputFuelErrorText = (TextView) findViewById(R.id.inputFuelErrorText);

        Button inputBtn = (Button) findViewById(R.id.inputFuelBtnOk);
        inputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(inputFuelText.getText() == null || inputFuelText.getText().toString().equals("")){
                    inputFuelErrorText.setVisibility(View.VISIBLE);
                    inputFuelText.setText("Please input fuel payment");
                    return;
                }

                new BillAsyncTask().execute();

                Intent mainPage = new Intent(InputFuelActivity.this, OilInfoActivity.class);
                startActivity(mainPage);
                finish();
            }
        });
    }

    private class BillAsyncTask extends AsyncTask<Void, Void, Map<String, Object>> {

        private Map<String, Object> request = new HashMap<String, Object>();

        @Override
        protected Map<String, Object> doInBackground(Void... params) {

            SharedPreferences pref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String id = pref.getString("id", "");
            Integer price = Integer.parseInt(inputFuelText.getText().toString());

            request.put("id", id);
            request.put("bill", price);
            request.put("title", "사용자 직접 입력");

            String url = "http://14.49.39.74:8080/fuelBill/insert";
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
            super.onPostExecute(stringObjectMap);
            Log.d(TAG, stringObjectMap.toString());
            if((Boolean) stringObjectMap.get("result")){
                Log.d(TAG, "Bill Async task success");
            }
        }

    }

}
