package com.oilMap.server.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;

/**
 * Created by Francis on 2015-03-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/test/resources/spring/root-context.xml", "file:src/main/resources/spring/servlet-context.xml"})
public class UserControllerTest {
    
    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    private User user;

    @Before
    public void Before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        
        this.user = new User("Sung", "1q2w3e4r!", "sung@gmail.com");
        this.userService.insert(user);
    }

    @Test
    public void 사용자_조회() throws Exception {
        Integer pn = user.getPn();

        mockMvc.perform(get("/user/info/{pn}", pn).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.username").value(this.user.getUsername()))
                .andExpect(jsonPath("$.password").value(this.user.getPassword()))
                .andExpect(jsonPath("$.email").value(this.user.getEmail()));
        
    }
}
