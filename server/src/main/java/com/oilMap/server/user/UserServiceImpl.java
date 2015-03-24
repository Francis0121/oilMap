package com.oilMap.server.user;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Francis on 2015-03-24.
 */
@Service
public class UserServiceImpl extends SqlSessionDaoSupport implements  UserService {
    
    @Override
    public void insert(User user) {
        getSqlSession().insert("user.insert", user);
    }

    @Override
    public User selectOne(Integer pn) {
        return getSqlSession().selectOne("user.selectOne", pn);
    }

    @Override
    public void updatePassword(User user) {
        getSqlSession().update("user.updatePassword", user);
    }

}
