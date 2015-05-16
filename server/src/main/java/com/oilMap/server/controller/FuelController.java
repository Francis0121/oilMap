package com.oilMap.server.controller;

import com.oilMap.server.bill.FuelBill;
import com.oilMap.server.bill.FuelBillService;
import com.oilMap.server.util.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis on 2015-05-16.
 */
@Controller
@RequestMapping(value = "/fuelBill")
public class FuelController {
    
    @Autowired
    private FuelBillService fuelService;

    @Autowired
    private Http http;

    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insert(@RequestBody Map<String,  Object> request) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        fuelService.calculateBill(new FuelBill((String) request.get("id"), (String) request.get("title"), (Integer) request.get("bill")));
        response.put("result", true);
        return response;
    }
    
    @ResponseBody
    @RequestMapping(value = "/select", method = RequestMethod.POST)
    public Map<String, Object> selectData(@RequestBody Map<String, Object> request) throws Exception {
        Map<String, Object> response = new HashMap<String, Object>();
        response.putAll(fuelService.selectMainInfo((String) request.get("id")));
        response.put("result", true);
        Double avgGasoline = http.sendGet();
        response.put("avgGasoline", avgGasoline);
        return response;
    }
    
}
