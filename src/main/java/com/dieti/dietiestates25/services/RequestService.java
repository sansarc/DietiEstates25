package com.dieti.dietiestates25.services;

import com.dieti.dietiestates25.constants.Constants;
import com.dieti.dietiestates25.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class RequestService {

    private RequestService() {}

    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);
    private static final RestTemplate restTemplate = new RestTemplate();


    public static Response POST(String endpoint, String jsonPayload) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);

            logResponse(response);
            return new Response(response.getStatusCode().value(), response.getBody());
        } catch (RestClientException ex) {
            logger.error(ex.getMessage());
            return new Response(Constants.Codes.SERVER_ERROR, "");
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

            ResponseEntity<String> response = restTemplate.exchange(
                    urlWithParams,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            logResponse(response);
            return new Response(response.getStatusCode().value(), response.getBody());

        } catch (RestClientException ex) {
            logger.error(ex.getMessage());
            return new Response(Constants.Codes.SERVER_ERROR, "");
        }
    }


    private static void logResponse(ResponseEntity<String> response) {
        logger.info("Received response {}: {}", response.getStatusCode().value(), response.getBody());
    }
}
