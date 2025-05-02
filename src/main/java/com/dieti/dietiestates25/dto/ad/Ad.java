package com.dieti.dietiestates25.dto.ad;

import com.dieti.dietiestates25.dto.User;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ad extends AdInsert {
    private int id;
    @SerializedName("agentDetail") private User agent;
    private String region, province, cityName, coordinates;
    private boolean publicTransport350m, school350m, leisurePark350m;
    private List<Photo> photos;

    @Setter
    @Getter
    public static class SearchBy {
        private String agentEmail, type, region, province, city, address;
        private int id;
        @SerializedName("nrooms") private int nRooms;
        @SerializedName("nbathrooms") private int nBathrooms;
        @SerializedName("price") private double minPrice;
        private double maxPrice;

        public SearchBy() {}

        public SearchBy(String type, int nRooms, int nBathrooms, String region, String province, String city, String address, double minPrice, double maxPrice) {
            this.type = type;
            this.nRooms = nRooms;
            this.nBathrooms = nBathrooms;
            this.region = region;
            this.province = province;
            this.city = city;
            this.address = address;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }
    }
}

