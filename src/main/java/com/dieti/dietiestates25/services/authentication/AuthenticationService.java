package com.dieti.dietiestates25.services.authentication;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.dto.Response;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.*;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;

import static com.dieti.dietiestates25.services.RequestService.extractMessage;

public class AuthenticationService {

    protected AuthenticationService() {}

    public Response login(String email, String pwd) {
        LoginRequest loginRequest = new LoginRequest(email, pwd);
        String json = new Gson().toJson(loginRequest);

        Response response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;
        if (response.getStatusCode() == Constants.Codes.UNAUTHORIZED)
            return new Response(response.getStatusCode(), null);

        response.setMessage(extractMessage(response.getMessage()));

        VaadinSession.getCurrent().setAttribute("session_id", response.getMessage());
        VaadinSession.getCurrent().setAttribute("email", email);
        VaadinSession.getCurrent().setAttribute("first_name", "Test");
        VaadinSession.getCurrent().setAttribute("last_name", "TEST");
        VaadinSession.getCurrent().setAttribute("role", "U");

        return response;
    }


    public Response createUser(Signup user) {
        String json = new Gson().toJson(user);
        var response = RequestService.POST(Constants.ApiEndpoints.SIGNUP, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        response.setMessage(extractMessage(response.getMessage()));

        VaadinSession.getCurrent().setAttribute("email", user.getEmail());

        return response;
    }

    public Response confirmUser(String email, String otp, boolean isManagerOrAgent) {
        var params = new HashMap<String, String>();
        params.put("isManagerOrAgent", String.valueOf(isManagerOrAgent));
        var json = new Gson().toJson(new Otp(email, otp));

        var response = RequestService.POST(Constants.ApiEndpoints.OTP_CONFIRMATION, params, json);

        if (response.getStatusCode() == Constants.Codes.INTERNAL_SERVER_ERROR)
            return null;

        response.setMessage(extractMessage(response.getMessage()));
        return response;
    }

}