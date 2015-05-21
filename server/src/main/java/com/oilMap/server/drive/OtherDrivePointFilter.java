package com.oilMap.server.drive;

/**
 * Created by Francis on 2015-05-21.
 */
public class OtherDrivePointFilter {
    
    private String id; 
    
    private Double latitude;
    
    private Double longitude;

    public OtherDrivePointFilter() {
    }

    public OtherDrivePointFilter(String id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "OtherDrivePointFilter{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
