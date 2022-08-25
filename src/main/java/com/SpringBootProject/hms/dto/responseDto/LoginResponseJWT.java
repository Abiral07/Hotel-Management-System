package com.SpringBootProject.hms.dto.responseDto;

import java.io.Serializable;

public class LoginResponseJWT implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;


    private final Object roles;

    public LoginResponseJWT(String jwtToken, Object roles) {
        this.jwtToken = jwtToken;
        this.roles = roles;
    }

    public String getToken() {
        return this.jwtToken;
    }

    public Object getRoles() {
        return roles;
    }
}
