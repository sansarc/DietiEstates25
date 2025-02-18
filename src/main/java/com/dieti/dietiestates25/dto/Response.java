package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants;
import lombok.Getter;

@Getter
public class Response {

    private final int statusCode;
    private final String statusMessage;

    public Response(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public boolean ok() {
        return statusCode == Constants.Codes.OK;
    }

}
