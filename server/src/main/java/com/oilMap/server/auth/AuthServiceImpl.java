package com.oilMap.server.auth;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Francis on 2015-05-03.
 */
@Service
public class AuthServiceImpl extends SqlSessionDaoSupport implements AuthService {

    @Override
    public void insert(Auth auth) {
        getSqlSession().insert("auth.insert", auth);
    }

    @Override
    public void delete(Auth auth) {
        getSqlSession().delete("auth.delete", auth);
    }

    @Override
    public Auth selectIsExist(Auth auth) {
        List<Auth> authList = getSqlSession().selectList("auth.isExist", auth);
        if(authList.size() == 0){
            return null;
        }
        return authList.get(0);
    }

    @Override
    public void deleteAll() {

    }
}
