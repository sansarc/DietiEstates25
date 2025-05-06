package com.dieti.dietiestates25.dto.ad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    private String name;
    private String code;

    public City(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
