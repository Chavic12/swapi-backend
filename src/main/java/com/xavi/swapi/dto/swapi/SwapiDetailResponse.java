package com.xavi.swapi.dto.swapi;

import lombok.Data;

@Data
public class SwapiDetailResponse<T> {
    private String message;
    private SwapiResult<T> result;
}
