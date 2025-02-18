package com.dieti.dietiestates25.services;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public SessionResponse authenticate(String email, String pwd) {
        LoginRequest loginRequest = new LoginRequest(email, pwd);
        String json = new Gson().toJson(loginRequest);

        Response response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);
        SessionResponse sessionResponse = new Gson().fromJson(response.getStatusMessage(), SessionResponse.class);
        sessionResponse.setStatusCode(response.getStatusCode()); // consider making sessionResponse extend Response

        return sessionResponse;
    }

    public Response createUser(String firstName, String lastName, String email, String password) {
        SignupRequest signupRequest = new SignupRequest(firstName, lastName, email, password);
        String json = new Gson().toJson(signupRequest);

        return RequestService.POST(Constants.ApiEndpoints.SIGNUP, json);
    }

    public Response confirmUser(String email, String otp) {
        OtpRequest otpRequest = new OtpRequest(email, otp);
        Gson gson = new Gson();
        String jsonString = gson.toJson(otpRequest);
        JsonObject json = gson.fromJson(jsonString, JsonObject.class);

        return RequestService.GET(Constants.ApiEndpoints.OTP_CONFIRMATION, json);
    }

}