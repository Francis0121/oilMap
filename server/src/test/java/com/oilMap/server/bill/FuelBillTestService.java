package com.oilMap.server.bill;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
import com.oilMap.server.drive.DriveService;
import com.oilMap.server.drive.Driving;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Francis on 2015-05-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class FuelBillTestService {

    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService authService;
    private Auth auth  = new Auth("tjdrms0121@gmail.com", "김성근", "1234567890987654");;


    @Qualifier("driveServiceImpl")
    @Autowired
    private DriveService driveService;
    private Driving driving;
    
    @Qualifier("fuelBillServiceImpl")
    @Autowired
    private FuelBillService fuelBillService;

    @Before
    public void Before() {
            authService.insert(auth);
//        driving = new Driving(auth.getId(), 10.21, 10.33);
    }

    @Test
    @Transactional
    public void FUEL_BILL_테스트() throws Exception{
        fuelBillService.selectMainInfo(auth.getId());
    }
}
