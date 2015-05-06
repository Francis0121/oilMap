package com.oilMap.server.drive;

/**
 * Created by Francis on 2015-05-06.
 */
public class DrivePoint {

    private String id;
    
    /**
     * 급 가속 위치
     */
    private Integer position;
    
    private String inputDate;

    public DrivePoint() {
    }

    public DrivePoint(String id, Integer position) {
        this.id = id;
        this.position = position;
    }

    public DrivePoint(String id, Integer position, String inputDate) {
        this.id = id;
        this.position = position;
        this.inputDate = inputDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "DrivePoint{" +
                "id='" + id + '\'' +
                ", position=" + position +
                ", inputDate='" + inputDate + '\'' +
                '}';
    }
}
