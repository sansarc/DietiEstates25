package com.dieti.dietiestates25.dto.ad;

import lombok.Data;

import java.util.List;

@Data
public class Ad {
    String saleType;
    double price;
    String agent;

    String region;
    String city;
    String address;
    String zipcode;
    String coordinates;

    int dimension;
    int floor;

    boolean elevator;
    int nRooms;
    int nBathrooms;
    int garageSpots;
    char energyClass;
    String description;

    private boolean publicTransport350m;
    private boolean school350m;
    private boolean leisurePark350m;

    List<Photo> photos;
}
