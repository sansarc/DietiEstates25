package com.dieti.dietiestates25.services;

import com.dieti.dietiestates25.dto.Response;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;

public class RequestService {

    private RequestService() {}

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    public static Response POST(String endpoint, String json) {

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .sslContext(SSLContext.getDefault())
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            logger.info("Sending POST request to {} with payload: {}", endpoint, json);

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            logResponse(response);

            return new Response(response.statusCode(), response.body());

        } catch (Exception ex) {
            logger.error("Failed to POST to {}: {}", endpoint, ex.getMessage(), ex);
            throw new RuntimeException("Failed to POST to " + endpoint + ": " + ex.getMessage(), ex);
        }
    }

    public static Response GET(String endpoint, JsonObject json) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .sslContext(SSLContext.getDefault())
                    .build();

            if (json != null) {

                var parameters = new ArrayList<String>();

                for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                    String key = entry.getKey().replaceAll("\"", "");
                    JsonElement value = entry.getValue();
                    parameters.add(String.format("%s=%s", key, value.toString().replaceAll("\"", "")));
                }

                endpoint = endpoint + "?" + String.join("&", parameters);
                logger.info("Sending GET request to {} with payload: {}", endpoint, json);
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            logResponse(response);

            return new Response(response.statusCode(), response.body());

        } catch (Exception ex) {
            logger.error("Failed to GET to {}: {}", endpoint, ex.getMessage(), ex);
            throw new RuntimeException("Failed to GET to " + endpoint + ": " + ex.getMessage(), ex);
        }
    }

    private static void logResponse(HttpResponse<String> response) {
        logger.info("Received response {}: {}", response.statusCode(), response.body());
    }
}
