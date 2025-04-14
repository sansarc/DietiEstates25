package com.dieti.dietiestates25.services.authentication;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import com.google.gson.*;

import java.util.HashMap;

public class AuthenticationService {

    protected AuthenticationService() {}

    public SimpleResponse login(String email, String pwd) {
        User loginRequest = new User(email, pwd);
        String json = new Gson().toJson(loginRequest);

        var response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        if (response.ok()) {
            var entityResponse = response.parse(User.class);

            if (entityResponse != null) {
                User user = entityResponse.getFirstEntity();

                if (user != null) {
                    UserSession.init(user);
                    UserSession.setSessionId(entityResponse.getMessage());

                    if (UserSession.isManagerOrAgent())
                        UserSession.setPwd(pwd);
                }
            }
        }

        return response;
    }


    public SimpleResponse createUser(User user) {
        String json = new Gson().toJson(user);
        var response = RequestService.POST(Constants.ApiEndpoints.SIGNUP, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        UserSession.init(user);
        UserSession.setSessionId("temp session");

        return response;
    }

    public SimpleResponse confirmUser(String email, String otp) {
        var params = new HashMap<String, String>();
        params.put("isManagerOrAgent", String.valueOf(false));
        var json = new Gson().toJson(new Otp(email, otp));

        var response = RequestService.POST(Constants.ApiEndpoints.CONFIRM_USER, params, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse logout (String sessionId) {
        var params = new HashMap<String, String>();
        params.put("sessionId", sessionId);
        var response = RequestService.GET(Constants.ApiEndpoints.LOGOUT, params);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

}