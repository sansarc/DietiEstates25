package com.dieti.dietiestates25.services;


import com.dieti.dietiestates25.constants.ApiConstants;
import com.dieti.dietiestates25.dto.LoginRequest;
import com.google.gson.Gson;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AuthenticationService {
    public boolean authenticate(String email, String pwd) {

        try {
            LoginRequest loginRequest = new LoginRequest(email, pwd);
            String json = new Gson().toJson(loginRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ApiConstants.LOGIN_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("API response: " + response.statusCode());

            if (response.statusCode() == 200)
                VaadinSession.getCurrent().setAttribute("user", loginRequest);

            return response.statusCode() == 200;

        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

}