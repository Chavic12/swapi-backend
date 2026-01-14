package com.xavi.swapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VehicleDTO {
    private String uid;
    private String name;
    private String model;
    private String manufacturer;
    private String vehicleClass;
    private String length;
    private String crew;
    private String passengers;
    private String cargoCapacity;
    private String costInCredits;
    private String maxAtmospheringSpeed;
    private String consumables;
    private List<String> films;
    private List<String> pilots;
    private String url;
    private String created;
    private String edited;
}
