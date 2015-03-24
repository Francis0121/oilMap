package com.oilMap.server.user;

/**
 * Created by Francis on 2015-03-24.
 */
public interface UserService {

    /**
     * Insert User information
     *  
     * @param user
     *  username, password, email  
     */
    void insert(User user);

    /**
     * @param pn 
     *  User primary key
     * @return
     *  User Object pn, username, password, email, joinDate, UpdateDate
     */
    User selectOne(Integer pn);
}
