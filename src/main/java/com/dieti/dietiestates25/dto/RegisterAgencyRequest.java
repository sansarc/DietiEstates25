package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAgencyRequest {
    private String agencyName;
    private String vatNumber;
    private Signup signupRequest;

    public RegisterAgencyRequest(String agencyName, String vatNumber, Signup signupRequest) {
        this.agencyName = agencyName;
        this.vatNumber = vatNumber;
        this.signupRequest = signupRequest;
    }
}
