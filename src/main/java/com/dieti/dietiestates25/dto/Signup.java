package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signup {
    private String firstName;
    private String lastName;
    private String email;
    private String pwd;

    public Signup(String firstName, String lastName, String email, String pwd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
    }
}
