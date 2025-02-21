package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Response {

    private int statusCode;

    private String message;

    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public boolean ok() {
        return statusCode == Constants.Codes.OK;
    }

}
