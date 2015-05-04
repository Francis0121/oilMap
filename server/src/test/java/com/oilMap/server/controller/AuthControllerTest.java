package com.oilMap.server.controller;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
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
 * Created by Francis on 2015-05-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"file:src/test/resources/spring/root-context.xml", "file:src/main/resources/spring/servlet-context.xml"})
public class AuthControllerTest {

    @Autowired
    private Message message;

    @Autowired
    protected WebApplicationContext wac;
    private MockMvc mockMvc;

    @Qualifier(value = "mockAuthServiceImpl")
    @Autowired
    private AuthService authService;
    private Auth auth = new Auth("134569987654567898");
    
    @Before
    public void Before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        authService.deleteAll();
    }

    @Test
    public void 구글_OAUTH_아이디_입력() throws Exception{
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/auth/insert")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    public void 구글_OAUTH_아이디_입력_아이디_존재() throws Exception{
        authService.insert(auth);
        
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/auth/insert")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false));
    }

    @Test
    public void 구글_OAUTH_아이디_있는_경우() throws Exception{
        authService.insert(auth);

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/auth/isExist")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
      public void 구글_OAUTH_아이디_없는_경우() throws Exception{
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/auth/isExist")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(false));
    }

    @Test
    public void 구글_OAUTH_아이디_삭제() throws Exception{
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("id", auth.getId());
        byte[] requestBytes = TestUtil.convertObjectToJsonBytes(request);
        mockMvc.perform(post("/auth/delete")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(requestBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }
}
