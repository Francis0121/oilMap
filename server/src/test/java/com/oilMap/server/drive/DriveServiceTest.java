package com.oilMap.server.drive;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
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
 * Created by Francis on 2015-05-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/resources/spring/root-context.xml")
public class DriveServiceTest {

    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService authService;
    private Auth auth  = new Auth("tjdrms0121@gmail.com", "김성근", "1234567890987654");;
    
    @Qualifier("driveServiceImpl")
    @Autowired
    private DriveService driveService;
    private Driving driving;

    @Before
    public void Before() {
        authService.insert(auth);
        driving = new Driving(auth.getId(), 10.21, 10.33);
    }

    @Test
    @Transactional
    public void 운전_정보_입력() throws Exception{
        driveService.insertDriving(driving);
        List<Driving> drivingList = driveService.selectDrivingInfo(auth.getId());
        assertThat(drivingList.size(), is(1));
    }
    
    
    @Test
    @Transactional
    public void 가속도_지점_입력() throws Exception{
        driveService.insertDrivePoint(new DrivePoint(auth.getId(), 10.1, 10.2, 10.3, 10.4, 0));
        List<DrivePoint> drivePointList = driveService.selectDrivePoint(auth.getId());
        assertThat(drivePointList.size(), is(1));
    }
}
