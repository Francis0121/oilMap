package com.oilMap.client.gps;

/**
 * Created by Francis on 2015-05-25.
 */
public class GpsPosition {
    
    private Integer pn;
    
    private String id;
    
    private Double latitude;
    
    private Double longitude;

    private String time;

    public GpsPosition() {
    }

    public GpsPosition(String id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPn() {
        return pn;
    }

    public void setPn(Integer pn) {
        this.pn = pn;
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
        return "GpsPosition{" +
                "pn=" + pn +
                ", id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", time='" + time + '\'' +
                '}';
    }
}
