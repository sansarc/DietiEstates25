package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants.*;
import com.dieti.dietiestates25.services.logging.Log;
import com.google.gson.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SimpleResponse {

    private int statusCode;
    private String rawBody;

    public SimpleResponse() {}

    public SimpleResponse(int statusCode, String rawBody) {
        this.statusCode = statusCode;
        this.rawBody = rawBody;
    }

    public SimpleResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public SimpleResponse checkNullityBeforeReturning() {
        if (getStatusCode() == Codes.INTERNAL_SERVER_ERROR)
            return null;
        if (getStatusCode() == Codes.UNAUTHORIZED)
            return new SimpleResponse(getStatusCode(), null);

        return this;
    }

    public boolean ok() {
        return statusCode == Codes.OK;
    }

    public <T> EntityResponse<T> parse(Class<T> entityType) {
        try {

            JsonObject jsonObject = JsonParser.parseString(rawBody).getAsJsonObject();
            EntityResponse<T> response = new EntityResponse<>();

            if (jsonObject.has("message")) {
                response.setMessage(jsonObject.get("message").getAsString());
            }

            List<T> entities = new ArrayList<>();

            if (jsonObject.has("entities") && jsonObject.get("entities").isJsonArray()) {
                JsonArray entitiesArray = jsonObject.getAsJsonArray("entities");

                Gson gson = new Gson();
                for (JsonElement element : entitiesArray) {
                    T entity = gson.fromJson(element, entityType);
                    entities.add(entity);
                }
            }

            response.setEntities(entities);
            return response;

        } catch (Exception e) {
            Log.error("Unexpected error in parsing the response: " + e.getMessage());
            return null;
        }
    }

}
