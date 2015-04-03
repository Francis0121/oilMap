package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.oilMap.client.R;
import com.oilMap.client.info.NavigationActivity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class CarRegisterActivity extends Activity {

    UserFuel userfuel = new UserFuel();

    private int carInformInteger = 2000;
    private int costInformInteger = 20000;
    private int periodInformInteger = 5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_register);

        Spinner carInform = (Spinner)findViewById(R.id.regCar);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(
                this, R.array.car, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        carInform.setAdapter(adapter1);


        Spinner costInform = (Spinner)findViewById(R.id.regCost);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this, R.array.cost, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        costInform.setAdapter(adapter2);

        Spinner periodInform = (Spinner)findViewById(R.id.regPeriod);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(
                this, R.array.period, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        periodInform.setAdapter(adapter3);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void mOnClick(View v){

        userfuel.setDisplacement(carInformInteger);
        userfuel.setCost(costInformInteger);
        userfuel.setPeriod(periodInformInteger);

        switch (v.getId()) {
            case R.id.btnRegComplete:
                UserFuel userFuel = new UserFuel(carInformInteger, costInformInteger, periodInformInteger);
                new CarRegisterAsyncTask().execute(userFuel);
                Intent complete = new Intent(this, LoginActivity.class);
                startActivity(complete);
                finish();
                break;

            case R.id.btnRegCarClear:
                Intent clear = new Intent(this, LoginActivity.class);
                startActivity(clear);
                finish();
                break;
        }
    }

    private class CarRegisterAsyncTask extends AsyncTask<UserFuel, Void, Map<String, Object>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, Object> doInBackground(UserFuel... users) {

            if(users[0] == null){
                return null;
            }

            try {
                String url = getString(R.string.contextPath) + "/user/fuel/update";
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, users[0], Map.class);
                Map<String, Object> messages = responseEntity.getBody();
                return messages;

            } catch (Exception e) {
                Log.e("Error", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            Log.d("fuel/update", map.toString());

            if((Boolean)map.get("success")){
                String carRegYes = "차량 등록에 성공하였습니다.";
                Toast.makeText(CarRegisterActivity.this, carRegYes, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CarRegisterActivity.this, NavigationActivity.class);
                startActivity(intent);
                finish();
            }else{
                String carRegNo = "차량 등록에 실패하였습니다." + map.get("messages").toString();
                Toast.makeText(CarRegisterActivity.this, carRegNo, Toast.LENGTH_SHORT).show();
            }

        }


    }



}
