package com.example.odb_test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 나홍철 on 2015-04-27.
 */

public class Json_Data implements Serializable {

    JSONObject jsonOb = null;
    private double fuel_use = 0.0;
    private double rpm = 0.0;
    private double fuel = 0.0;
    private double distance = 0.0;
    private long time = 0;
    private Date d;

    public double getFuelUse() {
        return fuel_use;
    }

    public void setFuelUse(double fuel_use) {
        this.fuel_use = fuel_use;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTime(long time) {
        this.time = time % 1000;
    }

    public long getTime() {
        return time;
    }

    public String retJson() {
        //Json객체 셋팅 후 그 객체의 스트링 반환
        try {
            jsonOb = new JSONObject();
            jsonOb.put("fuel_use", getFuelUse());
            jsonOb.put("rpm", getRpm());
            jsonOb.put("fuel", getFuel());
            jsonOb.put("distance", getDistance());
            jsonOb.put("time", getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonOb.toString();
    }
}
