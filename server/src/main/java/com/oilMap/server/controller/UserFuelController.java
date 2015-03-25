package com.oilMap.server.controller;

import com.oilMap.server.user.fuel.UserFuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Francis on 2015-03-25.
 */
@Controller
@RequestMapping("/user/fuel")
public class UserFuelController {
    
    @Autowired
    private UserFuelService userFuelService;
}
