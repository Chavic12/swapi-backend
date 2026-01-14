package com.xavi.swapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PeopleDTO {
    private String uid;
    private String name;
    private String gender;
    private String skinColor;
    private String hairColor;
    private String height;
    private String eyeColor;
    private String mass;
    private String birthYear;
    private String homeworld;
    private List<String> films;
    private List<String> vehicles;
    private List<String> starships;
    private String url;
    private String created;
    private String edited;
}
