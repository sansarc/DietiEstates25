package com.dieti.dietiestates25.services.geoapify;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class GeoapifyService {

    public static final String API_KEY = "0bab6ac0bf7c44a89d7efd4091fe04af";
    private static final String BASE_URL = "https://api.geoapify.com/v2/places";

    public boolean isNearbyPOI(double latitude, double longitude, String category, int radius) {
        String url = String.format("%s?categories=%s&filter=circle:%f,%f,%d&apiKey=%s",
                BASE_URL, category, longitude, latitude, radius, API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode features = Objects.requireNonNull(response.getBody()).get("features");

        return features != null && !features.isEmpty();
    }

}
