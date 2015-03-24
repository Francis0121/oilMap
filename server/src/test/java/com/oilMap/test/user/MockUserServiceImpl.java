package com.oilMap.test.user;

import com.oilMap.server.user.User;
import com.oilMap.server.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francis on 2015-03-24.
 */
@Service("userServiceImpl")
public class MockUserServiceImpl implements UserService {
    
    private Integer userPn = 0;
    private List<User> userList = new ArrayList<User>();
    
    @Override
    public void insert(User user) {
        user.setPn(this.userPn);
        this.userList.add(user);
    }

    @Override
    public User selectOne(Integer pn) {
        return userList.get(pn);
    }

    @Override
    public void updatePassword(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
