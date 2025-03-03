package com.dieti.dietiestates25.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String pwd;

    public SignupRequest(String firstName, String lastName, String email, String pwd) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
    }
}
