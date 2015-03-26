package com.oilMap.client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-03-25.
 */
public class CarRegisterActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_register);
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

        EditText editCar = (EditText) findViewById(R.id.regCar);
        EditText editCost = (EditText) findViewById(R.id.regCost);
        EditText editPeriod = (EditText) findViewById(R.id.regPeriod);

        String regCar = editCar.getText().toString();
        String regCost = editCost.getText().toString();
        String regPeriod = editPeriod.getText().toString();

        switch (v.getId()) {
            case R.id.btnRegComplete:
                Intent complete = new Intent(this, LoginActivity.class);
                startActivity(complete);
                finish();
                break;

            case R.id.btnRegCarClear:
                editCar.setText("");
                editCost.setText("");
                editPeriod.setText("");
                break;
        }
    }
}
