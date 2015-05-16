package com.oilMap.server.bill;

import java.util.Map;

/**
 * Created by Francis on 2015-05-16.
 */
public interface FuelBillService {
    
    void insert(FuelBill fuelBill);

    FuelBill selectMaxFuelBill(String id);

    Map<String, Object> selectMainInfo(String id);
}
