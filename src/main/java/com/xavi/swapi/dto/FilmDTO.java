package com.xavi.swapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilmDTO {
    private String uid;
    private String title;
    private int episodeId;
    private String director;
    private String producer;
    private String releaseDate;
    private String openingCrawl;
    private List<String> characters;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private List<String> species;
    private String url;
    private String created;
    private String edited;
}
