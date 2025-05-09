package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.dto.User;
import com.dieti.dietiestates25.dto.ad.Ad;
import com.dieti.dietiestates25.dto.bid.Bid;
import com.dieti.dietiestates25.services.session.UserSession;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static final User TEST_USER = new User(
            "John", "Doe", "john@example.com", null,
            "ACME Agency", "123456789", "A", true
    );

    public static final User OTHER_USER = new User(
            "Jane", "Smith", "jane@example.com", null,
            "XYZ Agency", "987654321", "A", true
    );

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
        ad1.setAgent(UserSession.asUser());
        ad1.setPrice(100.0);
        ad1.setType("S");
        ad1.setNRooms(1);
        ad1.setNBathrooms(1);
        ad2.setId(2);
        ad2.setAgent(UserSession.asUser());
        ad2.setPrice(200.0);
        ad2.setType("R");
        ad2.setNRooms(2);
        ad2.setNBathrooms(2);

        return List.of(ad1, ad2);
    }

    public static List<Bid> mockBidsList() {
        var bid1 = new Bid();
        bid1.setId(1);
        bid1.setOfferer(TEST_USER.getEmail());
        bid1.setStatus("P");
        var bid2 = new Bid();
        bid2.setId(2);
        bid2.setOfferer(TEST_USER.getEmail());
        bid2.setStatus("A");
        var bid3 = new Bid();
        bid3.setId(3);
        bid3.setOfferer(TEST_USER.getEmail());
        bid3.setStatus("R");

        return List.of(bid1, bid2, bid3);
    }
}
