package com.xavi.swapi.dto.swapi;

import lombok.Data;

@Data
public class SwapiResult<T> {
    private T properties;
    private String description;
    private String uid;
}
