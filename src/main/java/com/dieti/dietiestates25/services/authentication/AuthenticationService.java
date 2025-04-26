package com.dieti.dietiestates25.services.authentication;


import com.dieti.dietiestates25.constants.Constants.ApiEndpoints;
import com.dieti.dietiestates25.constants.Constants.Codes;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.services.requests.RequestService;
import com.dieti.dietiestates25.services.session.UserSession;
import com.google.gson.*;

import java.io.Serializable;
import java.util.HashMap;

public class AuthenticationService {

    protected AuthenticationService() {}

    public SimpleResponse login(String email, String pwd) {
        User loginRequest = new User(email, pwd);
        String json = new Gson().toJson(loginRequest);

        var response = RequestService.POST(ApiEndpoints.LOGIN, json);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
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
        var response = RequestService.POST(ApiEndpoints.SIGNUP, json);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        UserSession.init(user);
        UserSession.setSessionId("temp session");

        return response;
    }

    public SimpleResponse confirmUser(String email, String otp) {
        var params = new HashMap<String, String>();
        params.put("isManagerOrAgent", String.valueOf(false));
        var json = new Gson().toJson(new Otp(email, otp));

        var response = RequestService.POST(ApiEndpoints.CONFIRM_USER, params, json);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse logout (String sessionId) {
        var params = new HashMap<String, Serializable>();
        params.put("sessionId", sessionId);
        var response = RequestService.GET(ApiEndpoints.LOGOUT, params);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse sendOTP(String email) {
        var params = new HashMap<String, Serializable>();
        params.put("email", email);
        params.put("key", "ChangePwd");
        
        var response = RequestService.GET(ApiEndpoints.GENERATE_OTP, params);

        if (response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;

        return response;
    }

    public SimpleResponse changePwd(String email, String newPwd, String otp) {
        String json = new Gson().toJson(new Otp.NewPassword(email, newPwd, otp));
        var response = RequestService.PUT(ApiEndpoints.CHANGE_PWD, json);

        return response.getStatusCode() == Codes.INTERNAL_SERVER_ERROR
                ? null
                : response;
    }
}