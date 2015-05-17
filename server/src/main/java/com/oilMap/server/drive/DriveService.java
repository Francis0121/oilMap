package com.oilMap.server.drive;

import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-06.
 */
public interface DriveService {

    void insertDriving(Driving driving);
    
    void insertDrivePoint(DrivePoint drivePoint);

    List<Driving> selectDrivingInfo(String id);

    List<DrivePoint> selectDrivePoint(String id);

    List<Driving> selectDrivingFromFuelBillPn(Integer fuelBillPn);

    Map<String, Object> selectPosition(String id);
}
