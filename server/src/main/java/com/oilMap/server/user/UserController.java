package com.oilMap.server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis on 2015-03-24.
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
    
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value="/info/{pn}", method = RequestMethod.GET)
    public User getInformation(@PathVariable(value = "pn") Integer pn ){
        User user = userService.selectOne(pn);
        logger.debug("select user information " + user);
        return user;
    }
    
    @ResponseBody
    @RequestMapping(value="/updatePassword", method = RequestMethod.POST)
    public Map<String, Object> updatePassword(@RequestBody User user){
        Map<String, Object> model = new HashMap<String, Object>();
        User getUser = userService.selectOne(user.getPn());
        if(getUser != null){
            logger.debug("user " + user + " getUser " + getUser);
            if(!getUser.getPassword().equals(user.getPassword())){
               model.put("success", false);
               return model;
            }
        }else{
            model.put("success", false);
            return model;
        }
        
        logger.debug("get updatePassword " + user);
        
        userService.updatePassword(user);
        
        
        model.put("success", true);
        return model;
    }
    
}
