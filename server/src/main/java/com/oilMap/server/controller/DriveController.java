package com.oilMap.server.controller;

import com.oilMap.server.drive.DrivePoint;
import com.oilMap.server.drive.Driving;
import com.oilMap.server.drive.DriveService;
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
@RequestMapping("/drive")
public class DriveController {
    
    private static Logger logger = LoggerFactory.getLogger(DriveController.class);
    
    @Autowired
    private DriveService drivingService;
    
    @ResponseBody
    @RequestMapping(value = "/driving", method = RequestMethod.POST)
    public Map<String, Object> driving(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<String, Object>();
        drivingService.insertDriving(new Driving((String) request.get("id"), (Double) request.get("distance"), (Double) request.get("fuelQuantity")));
        response.put("result", true);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/drivePoint", method = RequestMethod.POST)
    public Map<String, Object> drivePoint(@RequestBody Map<String, Object> request){
        Map<String, Object> response = new HashMap<String, Object>();
        DrivePoint drivePoint = new DrivePoint((String)request.get("id"), (Double) request.get("latitude"), (Double) request.get("longitude"), (Double) request.get("startSpeed"), (Double) request.get("endSpeed"));
        drivingService.insertDrivePoint(drivePoint);
        response.put("result", true);
        return response;
    }
}
