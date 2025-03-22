package com.dieti.dietiestates25.services;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@SuppressWarnings("LoggingSimilarMessage")
public class RequestService {

    private RequestService() {}

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
    private static final RestTemplate restTemplate = new RestTemplate();

    private static ResponseEntity<String> response;

    public static Response POST(String endpoint, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {}", endpoint, jsonPayload);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
            logResponse(response);
            return new Response(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new Response(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        }  catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            return new Response(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static Response POST(String endpoint, Map<String, String> params, String jsonPayload) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {} and parameters {}", urlWithParams, jsonPayload, params);

            ResponseEntity<String> response = restTemplate.postForEntity(urlWithParams, entity, String.class);
            logResponse(response);
            return new Response(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new Response(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            return new Response(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static Response GET(String endpoint, Map<String, String> params) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            logger.info("Requesting {} with parameters {}", endpoint, params);
            response = restTemplate.exchange(
                    urlWithParams,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            logResponse(response);
            return new Response(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new Response(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            return new Response(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    private static void logResponse(ResponseEntity<String> response) {
        logger.info("Received response {}: {}", response.getStatusCode().value(), response.getBody());
    }

    public static String extractMessage(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonObject.getString("message");
        } catch (JSONException ignored) {}

        return "";
    }
}
