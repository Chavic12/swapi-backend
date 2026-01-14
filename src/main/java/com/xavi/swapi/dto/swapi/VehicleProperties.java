package com.xavi.swapi.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VehicleProperties {
    private String name;
    private String model;
    private String manufacturer;

    @JsonProperty("vehicle_class")
    private String vehicleClass;

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

    private List<String> pilots;
    private List<String> films;
    private String url;
    private String created;
    private String edited;
}
