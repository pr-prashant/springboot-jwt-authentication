package com.example.springboot.jwt.application.beans;

import java.util.Date;

/**
 * @author Prashant Patel
 * Date: 7/22/2020
 **/
public class TokenBean {

    private String token;

    private Date expiryTime;

    public TokenBean(String token, Date expiryTime) {
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
}
