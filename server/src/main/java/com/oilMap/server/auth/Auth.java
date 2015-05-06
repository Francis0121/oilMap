package com.oilMap.server.auth;

/**
 * Created by Francis on 2015-05-03.
 */
public class Auth {
    
    private String id;
    
    private String registerDate;
    
    private String name;
    
    private String email;

    public Auth() {
    }

    public Auth(String id) {
        this.id = id;
    }

    public Auth(String email, String name, String id) {
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "id='" + id + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
