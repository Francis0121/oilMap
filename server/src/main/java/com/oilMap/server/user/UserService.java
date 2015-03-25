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
     * Select one user 
     * @param pn 
     *  User primary key
     * @return
     *  User Object pn, username, password, email, joinDate, UpdateDate
     */
    User selectOne(Integer pn);

    /**
     * Update Password 
     * @param user
     *  password, updatePassword
     */
    void updatePassword(User user);

    /**
     * Delete user
     * @param user
     *  user pn
     */
    void delete(User user);

    /**
     * Email Exist check
     * @param email
     * @return
     */
    Boolean selectIsExistEmail(String email);

    /**
     * Username exist checkt
     * @param username
     * @return
     */
    Boolean selectIsExistUsername(String username);


    /**
     * 새로운 패스워드 생성 및 업데이트
     * @param user
     * @return 새로운 비밀번호
     */
    String updateNewPassword(User user);
}
