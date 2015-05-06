package com.oilMap.test.auth;

import com.oilMap.server.auth.Auth;
import com.oilMap.server.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Francis on 2015-05-04.
 */
@Service("mockAuthServiceImpl")
public class MockAuthServiceImpl implements AuthService{
    
    private static Logger logger = LoggerFactory.getLogger(MockAuthServiceImpl.class);

    private List<Auth> authList = new ArrayList<Auth>();
    
    @Override
    public void insert(Auth auth) {
        if(auth.getId() == null || auth.getId().equals("")){
            throw new RuntimeException("Null Point Exception : id");
        }

        if(auth.getEmail() == null || auth.getEmail().equals("")){
            throw new RuntimeException("Null Point Exception : email");
        }

        if(auth.getName() == null || auth.getName().equals("")){
            throw new RuntimeException("Null Point Exception : name");
        }
        
        authList.add(auth);
    }

    @Override
    public void delete(Auth auth) {
        int deleteIndex = -1;
        for(int i=0; i<authList.size(); i++){
            Auth a = authList.get(i);
            if(a.getId().equals(auth.getId())) {
                deleteIndex = i;
            }
        }

        if(deleteIndex != -1)
            authList.remove(deleteIndex);
    }

    @Override
    public Auth selectIsExist(Auth auth) {
        for(Auth a : authList){
            if(a.getId().equals(auth.getId())){
                return a;
            }
        }
        return null;
    }

    @Override
    public void deleteAll() {
        authList = new ArrayList<Auth>();
    }


}
