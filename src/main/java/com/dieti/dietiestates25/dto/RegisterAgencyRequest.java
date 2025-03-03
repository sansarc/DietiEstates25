package com.dieti.dietiestates25.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAgencyRequest {
    private String agencyName;
    private String vatNumber;
    private SignupRequest signupRequest;

    public RegisterAgencyRequest(String agencyName, String vatNumber, SignupRequest signupRequest) {
        this.agencyName = agencyName;
        this.vatNumber = vatNumber;
        this.signupRequest = signupRequest;
    }
}
