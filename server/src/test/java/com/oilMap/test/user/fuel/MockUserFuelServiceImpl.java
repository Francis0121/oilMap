package com.oilMap.test.user.fuel;

import com.oilMap.server.user.fuel.UserFuel;
import com.oilMap.server.user.fuel.UserFuelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francis on 2015-03-25.
 */
@Service(value = "userFuelServiceImpl")
public class MockUserFuelServiceImpl implements UserFuelService {

    private List<UserFuel> userFuels = new ArrayList<UserFuel>();
    
    @Override
    public void insert(UserFuel userFuel) {
        UserFuel setUserFuel = new UserFuel(userFuel.getUserPn(), userFuel.getDisplacement(), userFuel.getCost(), userFuel.getCost());
        userFuels.add(setUserFuel);
    }

    @Override
    public UserFuel selectOne(Integer userPn) {
        
        return null;
    }

    @Override
    public void update(UserFuel userFuel) {
        for(UserFuel fuel : userFuels){
            if(fuel.getUserPn().equals(userFuel.getUserPn())){
                fuel.setCost(userFuel.getCost());
                fuel.setDisplacement(userFuel.getDisplacement());
                fuel.setPeriod(userFuel.getPeriod());
            }
        }
    }
}
