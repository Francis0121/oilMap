package com.oilMap.server.drive;

/**
 * Created by Francis on 2015-05-06.
 */
public class Driving {
    
    private String id;

    private Integer fuelBillPn;
    
    /**
     * 운행 종료 후 이동거리 
     */
    private Double distance;
    
    /**
     * 운행 종료 후 기름양
     */
    private Double fuelQuantity;
    
    private String inputDate;

    public Driving() {
    }

    public Driving(String id, Double distance, Double fuelQuantity) {
        this.id = id;
        this.distance = distance;
        this.fuelQuantity = fuelQuantity;
    }

    public Integer getFuelBillPn() {
        return fuelBillPn;
    }

    public void setFuelBillPn(Integer fuelBillPn) {
        this.fuelBillPn = fuelBillPn;
    }

    public Double getFuelQuantity() {
        return fuelQuantity;
    }

    public void setFuelQuantity(Double fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    @Override
    public String toString() {
        return "Driving{" +
                "id='" + id + '\'' +
                ", fuelBillPn=" + fuelBillPn +
                ", distance=" + distance +
                ", fuelQuantity=" + fuelQuantity +
                ", inputDate='" + inputDate + '\'' +
                '}';
    }
}
