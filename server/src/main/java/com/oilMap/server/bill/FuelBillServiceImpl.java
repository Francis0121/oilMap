package com.oilMap.server.bill;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-05-16.
 */
@Service
public class FuelBillServiceImpl extends SqlSessionDaoSupport implements FuelBillService{

    @Override
    public void insert(FuelBill fuelBill) {
        getSqlSession().insert("fuelBill.insert", fuelBill);
    }

    @Override
    public FuelBill selectMaxFuelBill(String id) {
        return getSqlSession().selectOne("fuelBill.selectMaxFuelBill", id);
    }
}
