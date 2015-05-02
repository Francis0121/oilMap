package com.oilMap.server.auth;

/**
 * Created by Francis on 2015-05-03.
 */
public interface AuthService {
    
    void insert(Auth auth);
    
    void delete(Auth auth);
    
    Auth selectIsExist(Auth auth);
}
