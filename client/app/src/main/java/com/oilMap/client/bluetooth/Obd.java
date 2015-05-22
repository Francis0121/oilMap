package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-27.
 */
public class Obd{

    private double latitude;
    private double longitude;

   // private double fuel_efficiency=0.0;
    private double fuel = 0.0;
    private double fuel_consumption = 0.0;
    //private double fuel_level = 0.0;
    private double distance = 0.0;
    private long time = 0;
    //private double speed = 0.0;


    /*
    public double getFuelEfficiency() {
        return fuel_efficiency;
    }

    public void setFuelEfficiency(double fuel_efficiency) {
        this.fuel_efficiency = fuel_efficiency;
    }*/

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getFuelConsumption() {
        return fuel_consumption;
    }

    public void setFuelConsumption(double fuel_consumption) {
        this.fuel_consumption = fuel_consumption;
    }
/*
    public double getFuelLevel() {
        return fuel_level;
    }

    public void setFuelLevel(double fuel_level) {
        this.fuel_level = fuel_level;
    }
*/
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
   /* public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
*/
    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
