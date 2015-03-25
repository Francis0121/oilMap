package com.oilMap.server.controller;

import com.oilMap.server.user.User;
import com.oilMap.server.user.UserService;
import com.oilMap.server.user.fuel.UserFuel;
import com.oilMap.server.util.Message;
import com.oilMap.server.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Francis on 2015-03-25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/test/resources/spring/root-context.xml", "file:src/main/resources/spring/servlet-context.xml"})
public class UserControllerFuelTest {
    
    @Autowired
    private Message message;

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;
    
    @Autowired
    private UserService userService;
    private User user;
    
    private UserFuel userFuel;
    
    @Before
    public void Before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        this.user = new User("Sung", "1q2w3e4r!", "sung@gmail.com");
        userService.insert(this.user);

        this.userFuel = new UserFuel(user.getPn(), 1000, 2000, 7);
    }
    
    @Test
    public void 유류정보_입력() throws Exception{
        mockMvc
                .perform(post("/user/fuel/insert")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(userFuel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    public void 유류정보가_존재하지_않는_경우() throws Exception{
        mockMvc
                .perform(post("/user/fuel/insert")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(new UserFuel())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.messages.displacement").value(message.getValue("userFuel.displacement.notEmpty")))
                .andExpect(jsonPath("$.messages.cost").value(message.getValue("userFuel.cost.notEmpty")))
                .andExpect(jsonPath("$.messages.period").value(message.getValue("userFuel.period.notEmpty")));
    }
    
    @Test
    public void 유류정보_업데이트() throws Exception{
        mockMvc
                .perform(post("/user/fuel/update")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(userFuel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
    
}
