package com.oilMap.server.controller;

import com.oilMap.server.user.fuel.UserFuel;
import com.oilMap.server.user.fuel.UserFuelService;
import com.oilMap.server.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Francis on 2015-03-25.
 */
@Controller
@RequestMapping("/user/fuel")
public class UserFuelController {
    
    private static Logger logger = LoggerFactory.getLogger(UserFuelController.class);
    
    @Autowired
    private UserFuelService userFuelService;
    
    @Autowired
    private Message message;
    
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Map<String, Object> insert(@RequestBody UserFuel userFuel, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        new Validator(){
            
            @Override
            public boolean supports(Class<?> aClass) {
                return UserFuel.class.isAssignableFrom(aClass);
            }

            @Override
            public void validate(Object object, Errors errors) {
                UserFuel fuel = (UserFuel) object;

                Integer displacement = fuel.getDisplacement();
                if(displacement == null){
                    errors.rejectValue("displacement", "userFuel.displacement.notEmpty");
                }
                
                Integer cost = fuel.getCost();
                if(cost == null){
                    errors.rejectValue("cost", "userFuel.cost.notEmpty");
                }

                Integer period = fuel.getCost();
                if(period == null){
                    errors.rejectValue("period", "userFuel.period.notEmpty");
                }
            }
            
        }.validate(userFuel, result);

        if(result.hasErrors()){
            map.put("success", false);

            Map<String, String> messages = new HashMap<String, String>();
            for(FieldError error : result.getFieldErrors()){
                messages.put(error.getField(), message.getValue(error.getCode()));
                logger.debug(error.getField() + message.getValue(error.getCode()));
            }
            logger.debug(messages.toString());
            map.put("messages", messages);
            return map;
        }
        
        userFuelService.insert(userFuel);
        map.put("success", true);
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody UserFuel userFuel, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        new Validator(){

            @Override
            public boolean supports(Class<?> aClass) {
                return UserFuel.class.isAssignableFrom(aClass);
            }

            @Override
            public void validate(Object object, Errors errors) {
                UserFuel fuel = (UserFuel) object;

                Integer displacement = fuel.getDisplacement();
                if(displacement == null){
                    errors.rejectValue("displacement", "userFuel.displacement.notEmpty");
                }

                Integer cost = fuel.getCost();
                if(cost == null){
                    errors.rejectValue("cost", "userFuel.cost.notEmpty");
                }

                Integer period = fuel.getCost();
                if(period == null){
                    errors.rejectValue("period", "userFuel.period.notEmpty");
                }
            }

        }.validate(userFuel, result);

        if(result.hasErrors()){
            map.put("success", false);

            Map<String, String> messages = new HashMap<String, String>();
            for(FieldError error : result.getFieldErrors()){
                messages.put(error.getField(), message.getValue(error.getCode()));
                logger.debug(error.getField() + message.getValue(error.getCode()));
            }
            logger.debug(messages.toString());
            map.put("messages", messages);
            return map;
        }

        userFuelService.update(userFuel);
        map.put("success", true);
        return map;
    }
}
