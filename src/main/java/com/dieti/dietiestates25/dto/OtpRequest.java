package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OtpRequest {

    private String email;
    private String otp;

    public OtpRequest(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

}
