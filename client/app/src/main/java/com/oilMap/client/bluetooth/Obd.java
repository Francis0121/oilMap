package com.oilMap.client.bluetooth;

/**
 * Created by 정성진 on 2015-04-27.
 */
public class Obd{

    private double distance;
    private double speed;
    private int rpm;
    private double fuelEfficiency;
    private long time;
    private int numOfAcceleration;
    private int numOfDeceleration;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getRpm() {
        return rpm;
    }

    public void setRpm(int rpm) {
        this.rpm = rpm;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getNumOfAcceleration() {
        return numOfAcceleration;
    }

    public void setNumOfAcceleration(int numOfAcceleration) {
        this.numOfAcceleration = numOfAcceleration;
    }

    public int getNumOfDeceleration() {
        return numOfDeceleration;
    }

    public void setNumOfDeceleration(int numOfDeceleration) {
        this.numOfDeceleration = numOfDeceleration;
    }
}
