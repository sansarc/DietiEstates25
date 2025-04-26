package com.dieti.dietiestates25.dto;

import com.dieti.dietiestates25.constants.Constants.*;
import com.dieti.dietiestates25.services.logging.Log;
import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SimpleResponse {

    protected int statusCode;
    protected String rawBody;

    public SimpleResponse() {}

    public SimpleResponse(int statusCode, String rawBody) {
        this.statusCode = statusCode;
        this.rawBody = rawBody;
    }

    public boolean ok() {
        return statusCode == Codes.OK || statusCode == Codes.CREATED;
    }

    public <T> EntityResponse<T> parse(Class<T> entityType) {
        try {
            if (this.rawBody == null || this.rawBody.isEmpty()) {
                Log.warn(SimpleResponse.class, "Error while parsing: Raw response body is null or empty");
                return null;
            }

            var jsonElement = JsonParser.parseString(this.rawBody);
            var response = new EntityResponse<T>();

            response.setStatusCode(this.statusCode);  // Copy status code from original response
            response.setRawBody(this.rawBody);       // Copy raw body from original response

            var entities = new ArrayList<T>();
            var gson = new Gson();

            if (jsonElement.isJsonObject()) {    // case where response is a JSON object
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                if (jsonObject.has("message"))
                    response.setMessage(jsonObject.get("message").getAsString());

                if (jsonObject.has("entities") && jsonObject.get("entities").isJsonArray()) {
                    JsonArray entitiesArray = jsonObject.getAsJsonArray("entities");
                    for (JsonElement element : entitiesArray) {
                        T entity = gson.fromJson(element, entityType);
                        entities.add(entity);
                    }
                }
            }

            else if (jsonElement.isJsonArray()) {       // case where response is directly a JSON array
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    T entity = gson.fromJson(element, entityType);
                    entities.add(entity);
                }
            }

            response.setEntities(entities);
            return response;

        } catch (Exception e) {
            Log.error(SimpleResponse.class, "Unexpected error while parsing the response: " + e.getMessage());
            return null;
        }
    }
}
