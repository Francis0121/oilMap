package com.oilMap.server.auth;

/**
 * Created by Francis on 2015-05-03.
 */
public class Auth {
    
    private String id;
    
    private String registerDate;

    public Auth() {
    }

    public Auth(String id) {
        this.id = id;
    }

    public Auth(String id, String registerDate) {
        this.id = id;
        this.registerDate = registerDate;
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

    @Override
    public String toString() {
        return "Auth{" +
                "id='" + id + '\'' +
                ", registerDate='" + registerDate + '\'' +
                '}';
    }
}
