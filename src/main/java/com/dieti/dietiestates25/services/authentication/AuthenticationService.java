package com.dieti.dietiestates25.services.authentication;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;

public class AuthenticationService {

    protected AuthenticationService() {}

    public SimpleResponse login(String email, String pwd) {
        User loginRequest = new User(email, pwd);
        String json = new Gson().toJson(loginRequest);

        var response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;
        if (response.getStatusCode() == Constants.Codes.UNAUTHORIZED)
            return new SimpleResponse(response.getStatusCode(), null);

        var entityResponse = response.parse(User.class);

        if (entityResponse != null) {
            VaadinSession.getCurrent().setAttribute("session_id", entityResponse.getMessage());

            User user = entityResponse.getFirstEntity();

            if (user != null) {
                new UserSession(user);
            }
        }

        return response;
    }


    public SimpleResponse createUser(User user) {
        String json = new Gson().toJson(user);
        var response = RequestService.POST(Constants.ApiEndpoints.SIGNUP, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        VaadinSession.getCurrent().setAttribute("user", user);

        return response;
    }

    public SimpleResponse confirmUser(String email, String otp, boolean isManagerOrAgent) {
        var params = new HashMap<String, String>();
        params.put("isManagerOrAgent", String.valueOf(isManagerOrAgent));
        var json = new Gson().toJson(new Otp(email, otp));

        var response = RequestService.POST(Constants.ApiEndpoints.OTP_CONFIRMATION, params, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

}