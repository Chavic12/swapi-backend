package com.xavi.swapi.dto.swapi;

import lombok.Data;

@Data
public class FilmResultItem {
    private String uid;
    private FilmProperties properties;
    private String description;
}
