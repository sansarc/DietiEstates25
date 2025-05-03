package com.dieti.dietiestates25.utils;

import com.dieti.dietiestates25.dto.SimpleResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {

    public static final String SUCCESS_RESPONSE_JSON = """
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
}
