package com.oilMap.server.bill;

import com.oilMap.server.drive.DrivePoint;
import com.oilMap.server.drive.DriveService;
import com.oilMap.server.drive.Driving;
import com.oilMap.server.util.Http;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francis on 2015-05-16.
 */
@Service
public class FuelBillServiceImpl extends SqlSessionDaoSupport implements FuelBillService{

    @Autowired
    private DriveService driveService;
    
    @Autowired
    private Http http;

    @Override
    public void insert(FuelBill fuelBill) {
        getSqlSession().insert("fuelBill.insert", fuelBill);
    }

    @Override
    public FuelBill selectMaxFuelBill(String id) {
        return getSqlSession().selectOne("fuelBill.selectMaxFuelBill", id);
    }

    @Override
    public Map<String, Object> selectMainInfo(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        FuelBill fuelBill = selectMaxFuelBill(id);
        if(fuelBill != null && fuelBill.getPn() != null) {
            map.put("fuelBill", fuelBill);
            List<Driving> drivingList = driveService.selectDrivingFromFuelBillPn(fuelBill.getPn());
            map.put("drivingList", drivingList);
        }else{
            map.put("fuelBill", new FuelBill());
            map.put("drivingList", new ArrayList<Driving>());
        }

        return map;
    }

    @Override
    public void calculateBill(FuelBill fuelBill) throws Exception {
        
        Map<String, Object> map = selectMainInfo(fuelBill.getId());

        FuelBill getFuelBill = (FuelBill) map.get("fuelBill");
        if(map.get("fuelBill") != null && getFuelBill.getPn() != null) {
            Double avgGasoline = http.sendGet();
            List<Driving> drivingList = (List<Driving>) map.get("drivingList");
            Double totalCash = 0.0;
            for (int i = 0; i < drivingList.size() - 1; i++) {
                Driving drivingBefore = drivingList.get(i);
                Driving drivingEnd = drivingList.get(i + 1);
                Double calculate = drivingBefore.getFuelQuantity() - drivingEnd.getFuelQuantity();
                Double cash = (calculate * avgGasoline);
                logger.debug(calculate + " " + cash + " " + avgGasoline + " " + totalCash);
                totalCash += cash;
            }
            logger.debug(getFuelBill.toString());
            Double remainCash = getFuelBill.getBill() - totalCash;
            logger.debug("Remain Cash " +remainCash);
            fuelBill.setBill(fuelBill.getBill() + remainCash.intValue());
            logger.debug(fuelBill.toString());
        }
        
        insert(fuelBill);
    }
}
