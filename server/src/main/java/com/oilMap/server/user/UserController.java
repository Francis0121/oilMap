package com.oilMap.server.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    
}
