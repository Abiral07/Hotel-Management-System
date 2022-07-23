package com.SpringBootProject.hms.dto.responseDto;

import java.io.Serializable;

public class LoginResponseJWT implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;

    public LoginResponseJWT(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getToken() {
        return this.jwtToken;
    }
}
