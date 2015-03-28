package com.oilMap.test.user;

import com.oilMap.server.user.User;
import com.oilMap.server.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francis on 2015-03-24.
 */
@Service("userServiceImpl")
public class MockUserServiceImpl implements UserService {
    
    private static Logger logger = LoggerFactory.getLogger(MockUserServiceImpl.class);
    
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

    @Override
    public Integer selectIsExistUsername(String username) {
        for(User user : userList){
            String getUsername = user.getUsername();
            if(getUsername.equals(username)){
                return user.getPn();
            }
        }
        return null;
    }

    @Override
    public String updateNewPassword(User user) {
        String email = user.getEmail();
        String username = user.getUsername();
        for(User u : userList){
            String getEmail = u.getEmail();
            String getUsername = u.getUsername();
            if(getEmail.equals(email) && getUsername.equals(username)){
                u.setPassword("0p9o8i@");
                logger.debug("Set new Password");
                return "0p9o8i@";
            }
        }
        return null;
    }

    @Override
    public Boolean selectIsExistEmail(String email) {
        for(User user : userList){
            String getEmail = user.getEmail();
            if(getEmail.equals(email)){
                return true;
            }
        }
        return false;
    }
    
}
