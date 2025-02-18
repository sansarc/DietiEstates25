package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionResponse {

    private int statusCode;

    @SerializedName("sessionid")
    private final String sessionId;
    @SerializedName("message")
    private final String statusMessage;

    public SessionResponse(int statusCode, String sessionId, String statusMessage) {
        this.statusCode = statusCode;
        this.sessionId = sessionId;
        this.statusMessage = statusMessage;
    }

    public boolean ok() {
        return statusCode == Constants.Codes.OK;
    }
}
