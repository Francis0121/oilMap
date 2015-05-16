package com.oilMap.server.drive;

import com.oilMap.server.bill.FuelBill;
import com.oilMap.server.bill.FuelBillService;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Francis on 2015-05-06.
 */
@Service
public class DriveServiceImpl extends SqlSessionDaoSupport implements DriveService {

    @Autowired
    private FuelBillService fuelBillService;
    
    @Override
    public void insertDriving(Driving driving) {
        FuelBill fuelBill = fuelBillService.selectMaxFuelBill(driving.getId());
        driving.setFuelBillPn(fuelBill.getPn());
        getSqlSession().insert("drive.insertDriving", driving);
    }

    @Override
    public void insertDrivePoint(DrivePoint drivePoint) {
        getSqlSession().insert("drive.insertDrivePoint", drivePoint);
    }

    @Override
    public List<Driving> selectDrivingInfo(String id) {
        return getSqlSession().selectList("drive.selectDrivingInfo", id);
    }

    @Override
    public List<DrivePoint> selectDrivePoint(String id) {
        return getSqlSession().selectList("drive.selectDrivePoint", id);
    }

    @Override
    public List<Driving> selectDrivingFromFuelBillPn(Integer fuelBillPn) {
        return getSqlSession().selectList("drive.selectDrivingFromFuelBillPn", fuelBillPn);
    }
}
