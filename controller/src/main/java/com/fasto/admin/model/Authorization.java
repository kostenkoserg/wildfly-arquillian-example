package com.fasto.admin.model;

/**
 *
 * @author kostenko
 */
public class Authorization {
    
    private String token;
    
    public Authorization() {}
    
    public Authorization(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
