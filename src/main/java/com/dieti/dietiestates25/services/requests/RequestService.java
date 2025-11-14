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

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("LoggingSimilarMessage")
public class RequestService {

    public static final String UNEXPECTED_ERROR = "Unexpected error: {}";
    public static final String RECEIVED_RESPONSE = "Received response {}: {}";

    public RequestService() { /* default constructor */ }

    private final Logger logger = LoggerFactory.getLogger(RequestService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private ResponseEntity<String> response;

    public SimpleResponse POST(String endpoint, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Sending POST request to {} with payload {}", endpoint, jsonPayload);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        }  catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse POST(String endpoint, Map<String, String> params, String jsonPayload) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            String urlWithParams = builder.toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            logger.info("Sending POST request to {} with payload {} and parameters {}", urlWithParams, jsonPayload, params);

            response = restTemplate.postForEntity(urlWithParams, entity, String.class);
            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse POST(String endpoint, String headerName, String headerValue, String jsonPayload) {
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
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString().isEmpty() ? "" : ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse GET(String endpoint, Map<String, Serializable> params) {
        try {
            var builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            var urlWithParams = builder.toUriString();

            var headers = new HttpHeaders();
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
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError("Failed to fetch data from the server at " + endpoint + ".");
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse GET(String endpoint) {
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
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse PUT(String endpoint, String jsonPayload) {
        try {
            var headers = new HttpHeaders();
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
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse PUT(String endpoint, String headerName, String headerValue, Map<String, Serializable> params) {
        try {
            var builder = UriComponentsBuilder.fromUriString(endpoint);
            params.forEach(builder::queryParam);
            var urlWithParams = builder.toUriString();

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(headerName, headerValue);

            HttpEntity<?> entity = new HttpEntity<>(headers);
            logger.info("Sending GET request to {} with parameters {}", endpoint, params);

            response = restTemplate.exchange(
                    urlWithParams,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            logResponse(response);
            return new SimpleResponse(response.getStatusCode().value(), response.getBody());

        } catch (HttpClientErrorException ex) {
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    public SimpleResponse PUT(String endpoint, String headerName, String headerValue, String jsonPayload) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(headerName, headerValue);

            HttpEntity<?> entity = new HttpEntity<>(jsonPayload, headers);
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
            logger.warn(RECEIVED_RESPONSE, ex.getStatusCode().value(), ex.getResponseBodyAsString());
            return new SimpleResponse(ex.getStatusCode().value(), ex.getResponseBodyAsString());

        } catch (RuntimeException ex) {
            logger.error(UNEXPECTED_ERROR, ex.getMessage());
            NotificationFactory.criticalError(ex.getMessage());
            return new SimpleResponse(Constants.Codes.INTERNAL_SERVER_ERROR, "");
        }
    }

    private void logResponse(ResponseEntity<String> response) {
        var responseBody = response.getBody();

        if (responseBody == null)
            responseBody = "empty response body";
        else if (responseBody.length() > 500)
            responseBody = responseBody.substring(0, 200) + "...[truncated]";

        logger.info(RECEIVED_RESPONSE, response.getStatusCode().value(), responseBody);
    }
}