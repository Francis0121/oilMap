package com.example.odb_test;

import java.io.Serializable;

/**
 * Created by 정성진 on 2015-04-27.
 */

public class Obd_data implements Serializable {

    private double rpm;
    private double fuelEfficiency;

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public String returnData() {
        return ("fuel:"+(Double.toString(Double.parseDouble(String.format("%.1f", getFuelEfficiency()))))
                +"rpm:"+(Double.toString(Double.parseDouble(String.format("%.1f", getRpm())))));
    }

}
