package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EntityResponse<T> extends SimpleResponse {

    private String message;
    private List<T> entities;

    public EntityResponse() {
        super();
        this.message = "";
        this.entities = new ArrayList<>();
    }

    public EntityResponse(int statusCode, String rawBody) {
        super(statusCode, rawBody);
        this.message = "";
        this.entities = new ArrayList<>();
    }

    public T getFirstEntity() {
        return entities != null && !entities.isEmpty()
                ? entities.getFirst()
                : null;
    }

    @Override
    public boolean ok() {
        return super.ok() && entities != null;
    }
}
