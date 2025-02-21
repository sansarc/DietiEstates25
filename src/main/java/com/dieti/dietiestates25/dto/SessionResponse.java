package com.dieti.dietiestates25.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SessionResponse extends Response {

    @SerializedName("sessionid")
    private String sessionId;

    public SessionResponse(int statusCode, String sessionId, String message) {
        super(statusCode, message);
        this.sessionId = sessionId;
    }
}
