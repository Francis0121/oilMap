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
        User newUser = new User(user.getUsername(), user.getPassword(), user.getEmail());
        newUser.setPn(userPn++);
        this.userList.add(newUser);
        
        user.setPn(newUser.getPn());
    }

    @Override
    public User selectOne(Integer pn) {
        if(userList.size() < pn){
            return null;
        }
        return userList.get(pn);
    }

    @Override
    public void updatePassword(User user) {
        User updateUser = userList.get(user.getPn());
        updateUser.setPassword(user.getPassword());
    }

    @Override
    public void delete(User user) {

    }
}
