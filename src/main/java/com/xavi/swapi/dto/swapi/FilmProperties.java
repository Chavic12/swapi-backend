package com.xavi.swapi.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FilmProperties {
    private String title;

    @JsonProperty("episode_id")
    private int episodeId;

    private String director;
    private String producer;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("opening_crawl")
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
