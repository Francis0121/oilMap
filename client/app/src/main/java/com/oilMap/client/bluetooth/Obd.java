package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-27.
 */
public class Obd{

    private double rpm;
    private double fuel;
    private double distance;
    private double latitude;
    private double longitude;

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

    public double getDistance() { return distance; }

    public void setDistance(double distance) { this.distance = distance; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
