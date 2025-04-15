package com.dieti.dietiestates25.services.requests;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.SimpleResponse;
import com.dieti.dietiestates25.utils.NotificationFactory;
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

    public static SimpleResponse POST(String endpoint, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {}", endpoint, jsonPayload);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        }  catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse POST(String endpoint, Map<String, String> params, String jsonPayload) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {} and parameters {}", urlWithParams, jsonPayload, params);

            response = restTemplate.postForEntity(urlWithParams, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse POST(String endpoint, String headerName, String headerValue, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(headerName, headerValue);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {} and header {}:{}", endpoint, jsonPayload, headerName, headerValue);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse POST(String endpoint, String headerName, String headerValue, Map<String, String> params, String jsonPayload) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(headerName, headerValue);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {}, parameters {} and header {}:{}", urlWithParams, jsonPayload, params, headerName, headerValue);

            response = restTemplate.postForEntity(urlWithParams, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse GET(String endpoint, Map<String, String> params) {
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
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse GET(String endpoint) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            logger.info("Requesting {}", endpoint);
            response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public static SimpleResponse PUT(String endpoint, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Requesting {} with payload {}", endpoint, jsonPayload);

            response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn("Received response {}: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error("Unexpected error: {}", ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    private static void logResponse(ResponseEntity<String> response) {
        logger.info("Received response {}: {}", response.getStatusCode().value(), response.getBody());
    }
}