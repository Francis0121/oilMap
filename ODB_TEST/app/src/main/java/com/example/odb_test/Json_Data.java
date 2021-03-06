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
    String str=null;
    private double fuel_consumption=0.0;
    private double fuel = 0.0;
    private double rpm = 0.0;
    private double speed = 0.0;
    private double distance = 0.0;
    private long time = 0;


    public double getFuelConsumption() {
        return fuel_consumption;
    }

    public void setFuelConsumption(double fuel_consumption) {
        this.fuel_consumption = fuel_consumption;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
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
           // jsonOb.put("fuel_efficiency", getFuelEfficiency());     // 연비
            //jsonOb.put("fuel_level", getFuelLevel());               // 기름량 (%)
            //jsonOb.put("rpm", getRpm());                             // RPM
            jsonOb.put("fuel", getFuel());                           // 기름량 (L)
            jsonOb.put("distance", getDistance());                  // 총 거리
            jsonOb.put("time", getTime());                           // 시간
            jsonOb.put("fuel_consumption", getFuelConsumption());                 // 시간
            // java.util.ConcurrentModificationException 예외처리
            if(jsonOb.toString()==null)
                return "";
            str=jsonOb.toString();
            return str;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
