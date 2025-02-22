package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class SessionResponse {

    private final int statusCode;
    private final String message;
    @SerializedName("sessionid") private String sessionId;

    public SessionResponse(int statusCode, String message, String sessionId) {
        this.statusCode = statusCode;
        this.message = message;
        this.sessionId = sessionId;
    }

    public boolean ok() { return statusCode == Constants.Codes.OK; }

}
