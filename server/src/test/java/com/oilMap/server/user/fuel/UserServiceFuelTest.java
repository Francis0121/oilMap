package com.oilMap.server.user.fuel;

import com.oilMap.server.user.User;
import com.oilMap.server.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Francis on 2015-03-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class UserServiceFuelTest {

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;
    private User user;
    
    @Before
    public void Before() {
        user = new User("Sung", "1q2w3e4r!", "sung@gmail.com");
        userService.insert(user);
    }
    
    @Test
    public void 테스트() throws Exception{
        
        
    }
    
}
