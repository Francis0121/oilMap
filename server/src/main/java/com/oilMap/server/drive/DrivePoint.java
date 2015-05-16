package com.oilMap.server.drive;

/**
 * Created by Francis on 2015-05-06.
 */
public class DrivePoint {

    private String id;
    
    private Double latitude;
    
    private Double longitude;
    
    private Double startSpeed;
    
    private Double endSpeed;
    
    private String inputDate;

    public DrivePoint() {
    
    }

    public DrivePoint(String id, Double latitude, Double longitude, Double startSpeed, Double endSpeed) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.startSpeed = startSpeed;
        this.endSpeed = endSpeed;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(Double startSpeed) {
        this.startSpeed = startSpeed;
    }

    public Double getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(Double endSpeed) {
        this.endSpeed = endSpeed;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DrivePoint{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", startSpeed=" + startSpeed +
                ", endSpeed=" + endSpeed +
                ", inputDate='" + inputDate + '\'' +
                '}';
    }
}
