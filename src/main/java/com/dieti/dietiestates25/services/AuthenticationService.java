package com.dieti.dietiestates25.services;


import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.LoginRequest;
import com.dieti.dietiestates25.dto.OtpRequest;
import com.dieti.dietiestates25.views.upload.utils.Response;
import com.dieti.dietiestates25.dto.SignupRequest;
import com.google.gson.Gson;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public Response authenticate(String email, String pwd) {
        LoginRequest loginRequest = new LoginRequest(email, pwd);
        String json = new Gson().toJson(loginRequest);

        Response response = RequestService.POST(Constants.ApiEndpoints.LOGIN, json);

        if (response.getStatusCode() == 200)
            VaadinSession.getCurrent().setAttribute("user", loginRequest);

        return response;
    }

    public Response createUser(String firstName, String lastName, String email, String password) {
        SignupRequest signupRequest = new SignupRequest(firstName, lastName, email, password);
        String json = new Gson().toJson(signupRequest);

        return RequestService.POST(Constants.ApiEndpoints.SIGNUP, json);
    }

    public Response confirmUser(String email, String otp) {
        OtpRequest otpRequest = new OtpRequest(email, otp);
        String json = new Gson().toJson(otpRequest);

        return RequestService.POST(Constants.ApiEndpoints.OTP_CONFIRMATION, json);
    }

}