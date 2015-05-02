package com.oilMap.server.controller;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis on 2015-05-03.
 */
@Controller
@RequestMapping(value="/auth")
public class AuthController {
    
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @ResponseBody
    @RequestMapping(value = "/isExist", method = RequestMethod.POST)
    public Map<String, Object> isExist(@RequestBody Map<String, Object> request){
        Auth auth = new Auth((String)request.get("id"), (String)request.get("registerDate"));
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("result", authService.selectIsExist(auth));
        return response;
    }
    
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insert(@RequestBody Map<String, Object> request){
        Auth auth = new Auth((String)request.get("id"), null);
        authService.insert(auth);
        Map<String, Object> response = new HashMap<String, Object>();
        return response;
    }


    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(@RequestBody Map<String, Object> request){
        Auth auth = new Auth((String)request.get("id"), null);
        authService.delete(auth);
        Map<String, Object> response = new HashMap<String, Object>();
        return response;
    }
}
