package com.oilMap.server.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Francis on 2015-03-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class UserServiceTest {
    
    private static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    
    private User user;
    private User userOther;
    
    private User updateUser;
    private User deleteUser;
    
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

    @Test
    @Transactional
    public void 사용자_비밀번호_수정() throws Exception{
        updateUser = new User(user.getPn());
        updateUser.setUpdatePassword("2w3e4r5t@");
        userService.updatePassword(updateUser);
        
        User getUser = userService.selectOne(user.getPn());
        assertThat(getUser.getPassword(), is(updateUser.getUpdatePassword()));
    }
    
    @Test
    @Transactional
    public void 사용자_삭제() throws Exception{
        deleteUser = new User(user.getPn());
        userService.delete(deleteUser);
        
        User getUser = userService.selectOne(user.getPn());
        assertThat(null, is(getUser));
    }
    
    @Test
    @Transactional
    public void 이메일_존재() throws Exception{
        
        Boolean isExist = userService.selectIsExistEmail(user.getEmail());
        assertThat(isExist, is(true));

        isExist = userService.selectIsExistEmail("asd@naver.com");
        assertThat(isExist, is(false));
    }
    
    @Test
    @Transactional
    public void 유저이름_존재() throws Exception{
        Integer pn = userService.selectIsExistUsername(user.getUsername());
        assertThat(pn, is(user.getPn()));
        
        pn = userService.selectIsExistUsername("hell");
        assertThat(null, is(pn));
    }
    
    @Test
    @Transactional
    public void 새로운_비밀번호_생성() throws Exception{
        String newPassword = userService.updateNewPassword(user);
        assertThat(null, not(newPassword));
        logger.debug("New Password "+newPassword);
        
        user.setPassword(newPassword);
        User getUser = userService.selectOne(user.getPn());
        compareUser(user, getUser);
        
        user.setEmail("");
        newPassword = userService.updateNewPassword(user);
        assertThat(null, is(newPassword));
    }

    private void compareUser(User user, User getUser){
        assertThat(getUser.getUsername(), is(user.getUsername()));
        assertThat(getUser.getEmail(), is(user.getEmail()));
        assertThat(getUser.getPassword(), is(user.getPassword()));
    }
}
