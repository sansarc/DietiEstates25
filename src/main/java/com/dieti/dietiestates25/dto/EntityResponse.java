package com.dieti.dietiestates25.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class EntityResponse<T> extends SimpleResponse {

    private String message;
    private List<T> entities;

    public EntityResponse() {}

    public EntityResponse(int statusCode, String message, T entity) {
        super(statusCode);
        this.message = message;
        this.entities = Collections.singletonList(entity);
    }

    public T getFirstEntity() {
        return entities != null && !entities.isEmpty()
                ? entities.get(0)
                : null;
    }

}
