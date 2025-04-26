package com.dieti.dietiestates25.services.agency;

import com.dieti.dietiestates25.constants.Constants.*;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;

public class AgencyRequestsService {

    public SimpleResponse createAgency(RegisterAgency agency) {
        var json = new Gson().toJson(agency);
        var response = RequestService.POST(ApiEndpoints.CREATE_AGENCY, json);
        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse confirmManagerOrAgentAccount(String email, String oldPwd, String newPwd) {
        var params = new HashMap<String, String>();
        params.put("isManagerOrAgent", String.valueOf(true));
        var json = new Gson().toJson(new Otp.NewPassword(email, oldPwd, newPwd));

        var response = RequestService.POST(ApiEndpoints.CONFIRM_USER, params, json);
        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse createAgent(User user) {
        var json = new Gson().toJson(user);

        var response = RequestService.POST(ApiEndpoints.CREATE_AGENT, "sessionId", UserSession.getSessionId(), json);
        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public EntityResponse<User> getAgents(String agencyVAT) {
        var params = new HashMap<String, Serializable>();
        params.put("company", agencyVAT);
        var response = RequestService.GET(ApiEndpoints.GET_AGENTS, params);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;
        
        return response.parse(User.class);
    }
}
