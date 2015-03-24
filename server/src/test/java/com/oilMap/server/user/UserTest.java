package com.oilMap.server.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Francis on 2015-03-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class UserTest {

    @Autowired
    private UserService userService;
    
    private User user;
    private User userOther;
    
    private User updateUser;
    
    @Before
    public void Before() {
        user = new User("Sung", "1q2w3e4r!", "sung@gmail.com");
        userOther = new User("Temp", "1q2w3e4r!", "temp@gmail.com");
        
        userService.insert(user);
    }

    @Test
    @Transactional
    public void 사용자_입력() throws Exception {
        userService.insert(userOther);
        User getUser = userService.selectOne(userOther.getPn());
        compareUser(userOther, getUser);
    }
    
    private void compareUser(User user, User getUser){
        assertThat(getUser.getUsername(), is(user.getUsername()));
        assertThat(getUser.getEmail(), is(user.getEmail()));
        assertThat(getUser.getPassword(), is(user.getPassword()));
    }

    @Test
    @Transactional
    public void 사용자_비밀번호_수정() throws Exception{
        updateUser = new User(user.getPn());
        updateUser.setUpdatePassword("2w3e4r5t@");
        userService.updatePassword(updateUser);
        
        User getUser = userService.selectOne(user.getPn());
        assertThat(getUser.getPassword(), is(updateUser.getUpdatePassword()));
    }
}
