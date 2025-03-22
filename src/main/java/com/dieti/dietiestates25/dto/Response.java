package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
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
