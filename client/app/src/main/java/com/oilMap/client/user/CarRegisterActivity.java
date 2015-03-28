package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class CarRegisterActivity extends Activity {

    UserFuel userfuel = new UserFuel();

    int carInformInteger = 2000;
    int costInformInteger = 20000;
    int periodInformInteger = 5;

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
}
