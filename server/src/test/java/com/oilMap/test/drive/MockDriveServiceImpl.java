package com.oilMap.test.drive;

import com.oilMap.server.drive.DrivePoint;
import com.oilMap.server.drive.DriveService;
import com.oilMap.server.drive.Driving;
import com.oilMap.server.drive.GpsPosition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-16.
 */
@Service("mockDriveServiceImpl")
public class MockDriveServiceImpl implements DriveService{
    
    List<Driving> drivingList = new ArrayList<Driving>();
    List<DrivePoint> drivePointList = new ArrayList<DrivePoint>();

    @Override
    public void insertDriving(Driving driving) {
        drivingList.add(driving);
    }

    @Override
    public void insertDrivePoint(DrivePoint drivePoint) {
        drivePointList.add(drivePoint);
    }

    @Override
    public void insertGpsPosition(GpsPosition gpsPosition) {

    }

    @Override
    public List<Driving> selectDrivingInfo(String id) {
        List<Driving> newDrivingList = new ArrayList<Driving>();
        for(Driving driving : drivingList){
            if(driving.getId().equals(id)) {
                newDrivingList.add(driving);
            }
        }
        return newDrivingList;
    }

    @Override
    public List<DrivePoint> selectDrivePoint(String id) {
        List<DrivePoint> newDrivePoint = new ArrayList<DrivePoint>();
        for(DrivePoint drivePoint: drivePointList){
            if(drivePoint.getId().equals(id)) {
                newDrivePoint.add(drivePoint);
            }
        }
        return newDrivePoint;
    }

    @Override
    public List<Driving> selectDrivingFromFuelBillPn(Integer fuelBillPn) {
        return null;
    }

    @Override
    public Map<String, Object> selectPosition(String id) {
        return null;
    }

    @Override
    public Map<String, Object> selectOtherPosition(Map<String, Object> response) {
        return null;
    }

    @Override
    public Map<String, Object> selectGpsPositionToday(String id) {
        return null;
    }
}
