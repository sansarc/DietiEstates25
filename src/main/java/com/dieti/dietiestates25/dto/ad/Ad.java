package com.dieti.dietiestates25.dto.ad;

import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.ui_components.Form;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ad extends AdInsert {
    private int id;
    @SerializedName("agentDetail") private User agent;
    private String region;
    private String province;
    private String cityName;
    private String coordinates;
    private boolean publicTransport350m;
    private boolean school350m;
    private boolean leisurePark350m;
    private List<Photo> photos;

    @Setter
    @Getter
    public static class SearchBy {
        private String agent;
        private String type;
        private String region;
        private String province;
        private String city;
        private String address;
        private String locationAny;
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

        public SearchBy(String locationAny) {
            this.locationAny = Form.capitalize(locationAny);
        }
    }
}

