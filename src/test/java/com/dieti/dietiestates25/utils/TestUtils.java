package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.ad.Photo;
import com.dieti.dietiestates25.dto.bid.Bid;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static User newUser(String role) {
        return new User(
                "John", "Doe", "john@example.com", null,
                "ACME Agency", "123456789", role, true
        );
    }

    public static final String SUCCESS_JSON_MOCK = """
    {
        "entities": [],
        "message": "Operation successful."
    }
    """;


    public static SimpleResponse mockResponse(boolean isSuccess) {
        var response = mock(SimpleResponse.class);
        when(response.ok()).thenReturn(isSuccess);

        return response;
    }

    public static List<Ad> mockAdsList() {
        var ad1 = new Ad();
        var ad2 = new Ad();
        ad1.setId(1);
        ad1.setAgent(newUser("A"));
        ad1.setPrice(100.0);
        ad1.setType("S");
        ad1.setNRooms(1);
        ad1.setNBathrooms(1);
        ad2.setId(2);
        ad2.setAgent(newUser("A"));
        ad2.setPrice(200.0);
        ad2.setType("R");
        ad2.setNRooms(2);
        ad2.setNBathrooms(2);

        return List.of(ad1, ad2);
    }

    public static List<Bid> mockBidsList() {
        var bid1 = new Bid();
        bid1.setId(1);
        bid1.setOfferer("test1@example.com");
        bid1.setStatus("P");
        var bid2 = new Bid();
        bid2.setId(2);
        bid2.setOfferer("test2@example.com");
        bid2.setStatus("A");
        var bid3 = new Bid();
        bid3.setId(3);
        bid3.setOfferer("test3@example.com");
        bid3.setStatus("R");

        return List.of(bid1, bid2, bid3);
    }

    public static Ad mockAd() {
        Ad ad = new Ad();
        ad.setType("S");
        ad.setAgent(TestUtils.newUser("A"));
        ad.setId(1);
        ad.setDescription("Test Description");
        ad.setPhotos(List.of(new Photo("photo1.jpg", "base64"), new Photo("photo2.jpg", "base64")));
        ad.setNRooms(3);
        ad.setNBathrooms(2);
        ad.setDimensions(120);
        ad.setCoordinates("40.7128 ; -74.0060");
        ad.setAddress("123 Test Street");
        ad.setRegion("Test Region");
        ad.setProvince("Test Province");
        ad.setCityName("Test City");
        return ad;
    }
}
