package com.xavi.swapi.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class StarshipProperties {
    private String name;
    private String model;
    private String manufacturer;

    @JsonProperty("starship_class")
    private String starshipClass;

    private String length;
    private String crew;
    private String passengers;

    @JsonProperty("cargo_capacity")
    private String cargoCapacity;

    private String consumables;

    @JsonProperty("cost_in_credits")
    private String costInCredits;

    @JsonProperty("max_atmosphering_speed")
    private String maxAtmospheringSpeed;

    @JsonProperty("hyperdrive_rating")
    private String hyperdriveRating;

    @JsonProperty("MGLT")
    private String mglt;

    private List<String> pilots;
    private List<String> films;
    private String url;
    private String created;
    private String edited;
}
