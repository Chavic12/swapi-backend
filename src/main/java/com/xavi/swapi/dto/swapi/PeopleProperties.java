package com.xavi.swapi.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PeopleProperties {
    private String name;
    private String gender;

    @JsonProperty("skin_color")
    private String skinColor;

    @JsonProperty("hair_color")
    private String hairColor;

    private String height;

    @JsonProperty("eye_color")
    private String eyeColor;

    private String mass;
    private String homeworld;

    @JsonProperty("birth_year")
    private String birthYear;

    private List<String> vehicles;
    private List<String> starships;
    private List<String> films;
    private String url;
    private String created;
    private String edited;
}
