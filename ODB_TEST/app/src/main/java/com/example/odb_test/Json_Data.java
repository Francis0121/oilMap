package com.example.odb_test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 나홍철 on 2015-04-27.
 */

public class Json_Data implements Serializable {
    //문자열로부터 JSONObject 객체 생성
    JSONObject jsonOb = null;
    private double rpm = 0.0;
    private double fuel = 0.0;

    public double getRpm() {
        return rpm;
    }
    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public String retJson() {

        //////////////////////////////////////
        //문자열로부터 JSONObject 객체 생성

        try {
            jsonOb = new JSONObject();
            jsonOb.put("rpm", getRpm());
            jsonOb.put("fuel", getFuel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonOb.toString();
    }

}
