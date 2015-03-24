package com.oilMap.server.user;

import com.oilMap.server.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;

/**
 * Created by Francis on 2015-03-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/test/resources/spring/root-context.xml", "file:src/main/resources/spring/servlet-context.xml"})
public class UserControllerTest {
    
    private static Logger logger = LoggerFactory.getLogger(UserControllerTest.class);
    
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
    
    @Test
    public void 사용자_비밀번호_수정_성공() throws Exception{
        user.setUpdatePassword("2w3e4r5t@");
        
        mockMvc.perform(post("/user/updatePassword")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json;charset=UTF-8"))
                        .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void 사용자_비밀번호_잘못입력_실패() throws Exception{
        user.setPassword("12345!");
        user.setUpdatePassword("2w3e4r5t@");

        mockMvc.perform(post("/user/updatePassword")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    public void 사용자_고유번호_잘못된경우() throws Exception{
        user.setPn(99);
        
        mockMvc.perform(post("/user/updatePassword")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.success").value(false));
    }
}
