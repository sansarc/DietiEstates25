package com.dieti.dietiestates25.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAgencyRequest {
    @SerializedName("companyName")
    private String agencyName;
    private String vatNumber;
    private User manager;

    public RegisterAgencyRequest(String agencyName, String vatNumber, User manager) {
        this.agencyName = agencyName;
        this.vatNumber = vatNumber;
        this.manager = manager;
    }
}
