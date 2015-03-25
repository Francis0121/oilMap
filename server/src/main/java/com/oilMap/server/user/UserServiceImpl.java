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

    @Override
    public void delete(User user) {
        getSqlSession().delete("user.delete", user);
    }

    @Override
    public Boolean selectIsExistUsername(String username) {
        int count = getSqlSession().selectOne("user.selectIsExistUsername", username);
        return count > 0 ? true : false;
    }

    @Override
    public String updateNewPassword(User user) {
        User isUser = getSqlSession().selectOne("user.updateNewPassword", user);
        if(isUser == null || isUser.getPn() == null) {
            return null;
        }
        
        String newPassword = "";
        for (int j = 0; j < 8; j++) {
            newPassword += (char)((Math.random() * 26) + 97);
        }
        isUser.setUpdatePassword(newPassword);
        updatePassword(isUser);
        return newPassword;
    }

    @Override
    public Boolean selectIsExistEmail(String email) {
        int count = getSqlSession().selectOne("user.selectIsExistEmail", email);
        return count > 0 ? true : false;
    }

}
