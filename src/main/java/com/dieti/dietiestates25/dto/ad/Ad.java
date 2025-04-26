package com.dieti.dietiestates25.dto.ad;

import com.dieti.dietiestates25.dto.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ad extends AdInsert {
    private int id;
    private User agent;
    private String region, province, cityName, coordinates;
    private boolean publicTransport350m, school350m, leisurePark350m;
    private List<Photo> photos;

    public static class IdOnly {
        private int id;

        public IdOnly(int id) {
            this.id = id;
        }
    }
}

