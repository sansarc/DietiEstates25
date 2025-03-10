package com.dieti.dietiestates25.dto.ad;

import lombok.Data;

@Data
public class Photo {
    String name;
    byte[] imageData;

    public Photo(String name, byte[] imageData) {
        this.name = name;
        this.imageData = imageData;
    }
}
