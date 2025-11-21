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

    // -------------------------------------------------
    // Helper methods
    // -------------------------------------------------

    private String buildUrl(String endpoint, Map<String, ? extends Serializable> params) {
        var builder = UriComponentsBuilder.fromUriString(endpoint);
        if (params != null) params.forEach(builder::queryParam);
        return builder.toUriString();
    }

    private HttpHeaders buildHeaders(String headerName, String headerValue) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (headerName != null && headerValue != null) {
            headers.set(headerName, headerValue);
        }
        return headers;
    }

    // -------------------------------------------------
    // POST (JSON payload only)
    // -------------------------------------------------

    public SimpleResponse POST(String endpoint, String jsonPayload) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, buildHeaders(null, null));
            logger.info("Sending POST request to {} with payload {}", endpoint, jsonPayload);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
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

    // -------------------------------------------------
    // POST (with query params)
    // -------------------------------------------------

    public SimpleResponse POST(String endpoint, Map<String, String> params, String jsonPayload) {
        try {
            String urlWithParams = buildUrl(endpoint, params);
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, buildHeaders(null, null));

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

    // -------------------------------------------------
    // POST (with header)
    // -------------------------------------------------

    public SimpleResponse POST(String endpoint, String headerName, String headerValue, String jsonPayload) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, buildHeaders(headerName, headerValue));
            logger.info("Requesting {} with payload {} and header {}:{}", endpoint, jsonPayload, headerName, headerValue);

            response = restTemplate.postForEntity(endpoint, entity, String.class);
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

    // -------------------------------------------------
    // GET (with params)
    // -------------------------------------------------

    public SimpleResponse GET(String endpoint, Map<String, Serializable> params) {
        try {
            var urlWithParams = buildUrl(endpoint, params);
            var entity = new HttpEntity<>(buildHeaders(null, null));

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

    // -------------------------------------------------
    // GET (simple)
    // -------------------------------------------------

    public SimpleResponse GET(String endpoint) {
        try {
            HttpEntity<?> entity = new HttpEntity<>(buildHeaders(null, null));
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

    // -------------------------------------------------
    // PUT (JSON payload only)
    // -------------------------------------------------

    public SimpleResponse PUT(String endpoint, String jsonPayload) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, buildHeaders(null, null));
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

    // -------------------------------------------------
    // PUT (header + params)
    // -------------------------------------------------

    public SimpleResponse PUT(String endpoint, String headerName, String headerValue, Map<String, Serializable> params) {
        try {
            var urlWithParams = buildUrl(endpoint, params);
            var entity = new HttpEntity<>(buildHeaders(headerName, headerValue));

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

    // -------------------------------------------------
    // PUT (header + body)
    // -------------------------------------------------

    public SimpleResponse PUT(String endpoint, String headerName, String headerValue, String jsonPayload) {
        try {
            var entity = new HttpEntity<>(jsonPayload, buildHeaders(headerName, headerValue));

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

    // -------------------------------------------------
    // DELETE (header + params)
    // -------------------------------------------------

    public SimpleResponse DELETE(String endpoint, String headerName, String headerValue, Map<String, Serializable> params) {
        try {
            var urlWithParams = buildUrl(endpoint, params);
            var entity = new HttpEntity<>(buildHeaders(headerName, headerValue));

            logger.info("Sending DELETE request to {} with parameters {} and header {}:{}", urlWithParams, params, headerName, headerValue);

            response = restTemplate.exchange(
                    urlWithParams,
                    HttpMethod.DELETE,
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

    // -------------------------------------------------
    // Logging
    // -------------------------------------------------

    private void logResponse(ResponseEntity<String> response) {
        var responseBody = response.getBody();

        if (responseBody == null)
            responseBody = "empty response body";
        else if (responseBody.length() > 500)
            responseBody = responseBody.substring(0, 200) + "...[truncated]";

        logger.info(RECEIVED_RESPONSE, response.getStatusCode().value(), responseBody);
    }
}
