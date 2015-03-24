package com.oilMap.server.user;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Francis on 2015-03-24.
 */
public class User {

    /**
     * User Primary number 
     */
    private Integer pn;

    /**
     * User name unique
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Update Password
     */
    private String updatePassword;

    /**
     * Email address unique
     */
    @NotEmpty
    private String email;

    /**
     * join date
     */
    private String joinDate;

    /**
     * Update Date
     */
    private String updateDate;


    public User() {
    }
    
    public User(Integer pn) {
        this.pn = pn;
    }
    
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Integer getPn() {
        return pn;
    }

    public void setPn(Integer pn) {
        this.pn = pn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "pn=" + pn +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", updatePassword='" + updatePassword + '\'' +
                ", email='" + email + '\'' +
                ", joinDate='" + joinDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
