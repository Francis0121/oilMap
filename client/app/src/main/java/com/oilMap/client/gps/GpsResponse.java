package com.oilMap.client.gps;

import java.util.List;

/**
 * Created by Francis on 2015-05-25.
 */
public class GpsResponse {

    private Boolean result;

    private List<GpsPosition> gpsPositionList;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public List<GpsPosition> getGpsPositionList() {
        return gpsPositionList;
    }

    public void setGpsPositionList(List<GpsPosition> gpsPositionList) {
        this.gpsPositionList = gpsPositionList;
    }

    @Override
    public String toString() {
        return "GpsResponse{" +
                "result=" + result +
                ", gpsPositionList=" + gpsPositionList +
                '}';
    }
}
