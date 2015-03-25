package com.oilMap.server.user.fuel;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-03-25.
 */
@Service
public class UserFuelServiceImpl extends SqlSessionDaoSupport implements UserFuelService {

    @Override
    public void insert(UserFuel userFuel) {
        getSqlSession().insert("userFuel.insert", userFuel);
    }

    @Override
    public UserFuel selectOne(Integer userPn) {
        return getSqlSession().selectOne("userFuel.selectOne", userPn);
    }

    @Override
    public void update(UserFuel userFuel) {
        getSqlSession().update("userFuel.update", userFuel);
    }
}
