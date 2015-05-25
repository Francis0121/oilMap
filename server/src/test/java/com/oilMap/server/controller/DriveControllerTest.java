package com.oilMap.server.controller;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
import com.oilMap.server.drive.DrivePoint;
import com.oilMap.server.drive.Driving;
import com.oilMap.server.util.Message;
import com.oilMap.server.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Francis on 2015-05-16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/test/resources/spring/root-context.xml", "file:src/main/resources/spring/servlet-context.xml"})
public class DriveControllerTest {
    @Autowired
    private Message message;

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;

    @Qualifier(value = "mockAuthServiceImpl")
    @Autowired
    private AuthService authService;
    private Auth auth = new Auth("tjdrms0121@gmail.com","김성근","134569987654567898");

    @Before
    public void Before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        authService.insert(auth);
    }

    @Test
    public void 유류정보_입력() throws Exception{
        Driving driving =new Driving(auth.getId(), 10.21, 10.33);
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", driving.getId());
        request.put("distance", driving.getDistance());
        request.put("fuelQuantity", driving.getFuelQuantity());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/drive/driving")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    public void 가속도_지점_확인() throws Exception{
        DrivePoint drivePoint = new DrivePoint(auth.getId(), 10.1, 10.2, 10.3, 10.4, 0);
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", drivePoint.getId());
        request.put("latitude", drivePoint.getLatitude());
        request.put("longitude", drivePoint.getLongitude());
        request.put("startSpeed", drivePoint.getStartSpeed());
        request.put("endSpeed", drivePoint.getEndSpeed());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/drive/drivePoint")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }
    
}
