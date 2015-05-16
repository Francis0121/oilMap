package com.oilMap.client.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oilMap.client.R;

/**
 * Created by 김현준 on 2015-05-12.
 */
public class IdCheckActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_db_test);

        final IdCheckDBManager dbManager = new IdCheckDBManager(getApplicationContext(), "Food.db", null, 1);

        // DB에 저장 될 속성을 입력받는다
        final EditText etName = (EditText) findViewById(R.id.et_foodname);
        final EditText etPrice = (EditText) findViewById(R.id.et_price);

        // 쿼리 결과 입력
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);

        // Insert
        Button btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert into 테이블명 values (값, 값, 값...);
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                dbManager.insert("insert into FOOD_LIST values(null, '" + name + "', " + price + ");");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Update
        Button btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
                String name = etName.getText().toString();
                String price = etPrice.getText().toString();
                dbManager.update("update FOOD_LIST set price = " + price + " where name = '" + name + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Delete
        Button btnDelete = (Button) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String name = etName.getText().toString();
                dbManager.delete("delete from FOOD_LIST where name = '" + name + "';");

                tvResult.setText( dbManager.PrintData() );
            }
        });

        // Select
        Button btnSelect = (Button) findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tvResult.setText( dbManager.PrintData() );
            }
        });


    }
}
