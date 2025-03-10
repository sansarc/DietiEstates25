package com.dieti.dietiestates25.dto.ad;

import com.dieti.dietiestates25.dto.User;
import lombok.Data;

import java.util.List;

@Data
public class AdRequest {
    String saleType;
    double price;
    String agent;

    Location location;
    int dimension;
    int floor;

    boolean elevator;
    int nBedrooms;
    int nRoomsGeneral;
    int nBathrooms;
    int garageSpots;
    boolean disabledAmenities;
    char energyClass;
    String description;

    private boolean publicTransport350m;
    private boolean school350m;
    private boolean leisurePark350m;

    List<Photo> photos;


}
