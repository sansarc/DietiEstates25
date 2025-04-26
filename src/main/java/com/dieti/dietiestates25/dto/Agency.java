package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Agency {
    private String name;
    private String vatNumber;

    public Agency(String name, String vatNumber) {
        this.name = name;
        this.vatNumber = vatNumber;
    }
}
