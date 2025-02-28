package com.dieti.dietiestates25.services.authentication;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.*;
import com.dieti.dietiestates25.services.RequestService;
import com.google.gson.*;
import com.vaadin.flow.server.VaadinSession;

public class AuthenticationService {

    public SessionResponse authenticate(String email, String pwd) {
        LoginRequest loginRequest = new LoginRequest(email, pwd);
        String json = new Gson().toJson(loginRequest);

        Response response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SessionResponse.class, (JsonDeserializer<SessionResponse>) (jsonElement, typeOfT, context) -> {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            int statusCode = response.getStatusCode();
            String message = "";
            if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull())
                message = jsonObject.get("message").getAsString();
            String sessionId = (jsonObject.has("sessionid") && !jsonObject.get("sessionid").isJsonNull())
                    ? jsonObject.get("sessionid").getAsString()
                    : null;

            return new SessionResponse(statusCode, message, sessionId);
        });

        var sessionResponse = gsonBuilder.create().fromJson(response.getMessage(), SessionResponse.class);
        VaadinSession.getCurrent().setAttribute("session_id", sessionResponse.getSessionId());
        VaadinSession.getCurrent().setAttribute("email", email);

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