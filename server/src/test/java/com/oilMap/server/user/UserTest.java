package com.oilMap.server.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Francis on 2015-03-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class UserTest {

    @Autowired
    private UserService userService;

    @Before
    public void Before() {
        
    }

    @Test
    @Transactional
    public void 사용자_입력() throws Exception {
        userService.insert(new User("Sung", "1q2w3e4r!", "sung@gmail.com"));
    }
}
