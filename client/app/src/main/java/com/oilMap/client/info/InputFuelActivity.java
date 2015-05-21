package com.oilMap.client.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-05-21.
 */
public class InputFuelActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_fuel);

        EditText inputFuel = (EditText) findViewById(R.id.inputFuelText);

        Button inputBtn = (Button) findViewById(R.id.inputFuelBtnOk);

        inputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent mainPage = new Intent(InputFuelActivity.this, OilInfoActivity.class);
                startActivity(mainPage);
                finish();
            }});
    }
}
