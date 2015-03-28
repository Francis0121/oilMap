package com.oilMap.client.user;

/**
 * Created by 김현준 on 2015-03-26.
 */
public class User {
    /**
     * User Primary number
     */
    private Integer pn;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        this.updatePassword = updatePassword;
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

    /**
     * User name unique
     */
    private String username;

    /**
     * Password
     */
    private String password;

    /**
     * Password confirm
     */
    private String confirmPassword;

    /**
     * Update Password
     */
    private String updatePassword;

    /**
     * Email address unique
     */
    private String email;

    /**
     * join date
     */
    private String joinDate;

    /**
     * Update Date
     */
    private String updateDate;
}
