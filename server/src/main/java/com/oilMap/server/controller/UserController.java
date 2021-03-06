package com.oilMap.server.controller;

import com.oilMap.server.user.User;
import com.oilMap.server.user.UserService;
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
    
    @Autowired
    private UserFuelService userFuelService;
    
    @Autowired
    private Message message;
    
    @ResponseBody
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody User user, BindingResult result){
        logger.debug(user.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        new Validator(){
            @Override
            public boolean supports(Class<?> aClass) {
                return User.class.isAssignableFrom(aClass);
            }

            @Override
            public void validate(Object object, Errors errors) {
                User user = (User) object;
                
                String username = user.getUsername();
                String password = user.getPassword();
                Integer pn;
                
                // 아이디 입력 필요
                if(username == null || username.length() == 0 || username.equals("")){
                    errors.rejectValue("username", "user.username.notEmpty");
                }else{
                    // 아이디 존재하지않음
                    pn = userService.selectIsExistUsername(username);
                    if(pn == null){
                        errors.rejectValue("username", "user.username.notExist");
                    }else {
                        user.setPn(pn);
                        // 비밀번호틀림
                        if (password != null && password.length() != 0 && !password.equals("")) {
                            User getUser = userService.selectOne(pn);
                            logger.debug(getUser.toString());
                            if (!password.equals(getUser.getPassword())) {
                                errors.rejectValue("password", "user.password.notEqual");
                            }
                        }
                    }
                }
                
                // 비밀번호 입력 필요
                if (password == null || password.length() == 0 || password.equals("")) {
                    errors.rejectValue("password", "user.password.notEmpty");
                }
            }
        }.validate(user,result);
        
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
        
        map.put("success", true);
        map.put("pn", user.getPn());
        return map;
    }
    
    @ResponseBody
    @RequestMapping(value="/join", method = RequestMethod.POST)
    public Map<String, Object> join(@RequestBody User user, BindingResult result){
        logger.debug(user.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        new Validator(){
            @Override
            public boolean supports(Class<?> aClass) {
                return User.class.isAssignableFrom(aClass);
            }

            @Override
            public void validate(Object object, Errors errors) {
                User user = (User) object;
                String email = user.getEmail();
                if(email == null || email.length() == 0 || email.equals("")){
                    errors.rejectValue("email", "user.email.notEmpty");
                }else{
                   if(userService.selectIsExistEmail(email)){
                       errors.rejectValue("email", "user.email.exist");
                   }
                }
                
                String password = user.getPassword();
                String confirmPassword = user.getConfirmPassword();
                if(     (password == null || password.length() == 0 || password.equals("")) || 
                        (confirmPassword == null || confirmPassword.length() == 0 || confirmPassword.equals("")) ){
                    errors.rejectValue("password", "user.password.notEmpty");
                }else{
                    if(!password.equals(confirmPassword)){
                        errors.rejectValue("password", "user.password.notEqual");
                    }
                }
                
                String username = user.getUsername();
                if(username == null || username.length() == 0 || username.equals("")){
                    errors.rejectValue("username", "user.username.notEmpty");
                }else{
                    if(userService.selectIsExistUsername(username) != null){
                        errors.rejectValue("username", "user.username.exist");
                    }
                }
            }
            
        }.validate(user, result);
        
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
        
        userService.insert(user);
        userFuelService.insert(new UserFuel(user.getPn(), 0, 0, 0));
        map.put("pn", user.getPn());
        map.put("success", true);
        return map;
    }

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
    
    @ResponseBody
    @RequestMapping(value = "/updateNewPassword", method = RequestMethod.POST)
    public Map<String, Object> updateNewPassword(@RequestBody User user){
        Map<String, Object> map = new HashMap<String, Object>();
        String newPassword = userService.updateNewPassword(user);
        if(newPassword == null){
            map.put("success", false);
            map.put("messages", message.getValue("user.newPassword.notEqual"));
            return map;
        }
        
        map.put("success", true);
        map.put("newPassword", newPassword);
        return map;
    }
    
}
