package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String pwd;

    public LoginRequest(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

}

