package com.oilMap.test.user.fuel;

import com.oilMap.server.user.fuel.UserFuel;
import com.oilMap.server.user.fuel.UserFuelService;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-03-25.
 */
@Service(value = "userFuelServiceImpl")
public class MockUserFuelServiceImpl implements UserFuelService {

    @Override
    public void insert(UserFuel userFuel) {

    }

    @Override
    public UserFuel selectOne(Integer userPn) {
        return null;
    }

    @Override
    public void update(UserFuel userFuel) {

    }
}
