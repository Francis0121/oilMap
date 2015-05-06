package com.oilMap.server.controller;

import com.oilMap.server.drive.Driving;
import com.oilMap.server.drive.DrivingService;
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
 * Created by Francis on 2015-05-06.
 */
@Controller
@RequestMapping("drive")
public class DriveController {
    
    private static Logger logger = LoggerFactory.getLogger(DriveController.class);
    
    @Autowired
    private DrivingService drivingService;
    
    @ResponseBody
    @RequestMapping(value = "/driving", method = RequestMethod.POST)
    public Map<String, Object> driving(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<String, Object>();
        drivingService.insert(new Driving((String)request.get("id"), (Double)request.get("distance"), (Integer)request.get("fuelQuantity")));
        return response;
    }
    
}
