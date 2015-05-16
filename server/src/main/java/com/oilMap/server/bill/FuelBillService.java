package com.oilMap.server.bill;

/**
 * Created by Francis on 2015-05-16.
 */
public interface FuelBillService {
    
    void insert(FuelBill fuelBill);

    FuelBill selectMaxFuelBill(String id);
}
