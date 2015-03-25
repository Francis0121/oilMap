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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

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
    
    @Qualifier("userFuelServiceImpl")
    @Autowired
    private UserFuelService userFuelService;
    private UserFuel userFuel;
    
    @Before
    public void Before() {
        user = new User("Sung", "1q2w3e4r!", "sung@gmail.com");
        userService.insert(user);
        
        userFuel = new UserFuel(user.getPn(), 1000, 2000, 7);
    }

    @Test
    @Transactional
    public void 유류정보_입력() throws Exception{
        userFuelService.insert(userFuel);
        
        UserFuel getUserFuel = userFuelService.selectOne(userFuel.getUserPn());
        
        assertThat(getUserFuel.getUserPn(), is(userFuel.getUserPn()));
        assertThat(getUserFuel.getCost(), is(userFuel.getCost()));
        assertThat(getUserFuel.getDisplacement(), is(userFuel.getDisplacement()));
        assertThat(getUserFuel.getPeriod(), is(userFuel.getPeriod()));
        assertThat(null, not(getUserFuel.getUpdateDate()));
    }
    
    @Test
    @Transactional
    public void 유류정보_수정() throws Exception{
        userFuelService.insert(userFuel);
        UserFuel getUserFuel = userFuelService.selectOne(userFuel.getUserPn());
        
        userFuel.setDisplacement(2000);
        userFuel.setCost(30000);
        userFuel.setPeriod(5);
        userFuel.setUpdateDate(getUserFuel.getUpdateDate());
        
        userFuelService.update(userFuel);
        getUserFuel = userFuelService.selectOne(userFuel.getUserPn());

        assertThat(getUserFuel.getUserPn(), is(userFuel.getUserPn()));
        assertThat(getUserFuel.getCost(), is(userFuel.getCost()));
        assertThat(getUserFuel.getDisplacement(), is(userFuel.getDisplacement()));
        assertThat(getUserFuel.getPeriod(), is(userFuel.getPeriod()));
    }
    
}
