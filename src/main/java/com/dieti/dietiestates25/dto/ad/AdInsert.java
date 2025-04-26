package com.dieti.dietiestates25.dto.ad;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdInsert {
    private String type;
    private double price;

    private String city, address;

    @SerializedName("dimentions") private int dimensions;
    private int floor;

    private boolean AC, privateGarage, condominiumParking, doormanService;
    @SerializedName("lift") private boolean elevator;
    @SerializedName("nrooms") private int nRooms;
    @SerializedName("nbathrooms") private int nBathrooms;
    @SerializedName("energyclass") private String energyClass;
    private String description;

    public String getPriceAsString() {
        return String.format("%.2f", price) + "€";
    }
}