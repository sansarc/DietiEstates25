package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Otp {

    private String email;
    private String otp;

    public Otp(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

}
