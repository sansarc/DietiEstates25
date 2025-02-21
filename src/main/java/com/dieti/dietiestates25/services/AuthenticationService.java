package com.dieti.dietiestates25.services;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.google.gson.*;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public SessionResponse authenticate(String email, String pwd) {
        LoginRequest loginRequest = new LoginRequest(email, pwd);
        String json = new Gson().toJson(loginRequest);

        Response response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SessionResponse.class, (JsonDeserializer<SessionResponse>) (json1, typeOfT, context) -> {
            JsonObject jsonObject = json1.getAsJsonObject();
            String message = jsonObject.get("message").getAsString();
            String sessionId = jsonObject.get("sessionid").getAsString();
            int statusCode = response.getStatusCode();

            return new SessionResponse(statusCode, message, sessionId);
        });

        return gsonBuilder.create().fromJson(response.getMessage(), SessionResponse.class);
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