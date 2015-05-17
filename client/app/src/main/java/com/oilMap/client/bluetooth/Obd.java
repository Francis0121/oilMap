package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-27.
 */
public class Obd{

    private double latitude;
    private double longitude;

    private double fuel_efficiency=0.0;
    private double fuel = 0.0;
    private double rpm = 0.0;
    private double fuel_level = 0.0;
    private double distance = 0.0;
    private long time = 0;


    public double getFuelEfficiency() {
        return fuel_efficiency;
    }

    public void setFuelEfficiency(double fuel_efficiency) {
        this.fuel_efficiency = fuel_efficiency;
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

    public double getFuelLevel() {
        return fuel_level;
    }

    public void setFuelLevel(double fuel_level) {
        this.fuel_level = fuel_level;
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

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
