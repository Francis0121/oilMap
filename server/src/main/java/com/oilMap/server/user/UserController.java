package com.oilMap.server.user;

import com.oilMap.server.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.EncodedResource;
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
    private Message message;
    
    @ResponseBody
    @RequestMapping(value="/join", method = RequestMethod.POST)
    public Map<String, Object> join(@RequestBody User user, BindingResult result){
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
                if(password == null || password.length() == 0 || password.equals("")){
                    errors.rejectValue("password", "user.password.notEmpty");
                }
                
                String username = user.getUsername();
                if(username == null || username.length() == 0 || username.equals("")){
                    errors.rejectValue("username", "user.username.notEmpty");
                    if(userService.selectIsExistUsername(username)){
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
    
}
