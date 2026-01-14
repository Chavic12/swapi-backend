package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.VehicleDTO;
import com.xavi.swapi.dto.VehicleListItem;
import com.xavi.swapi.dto.swapi.SwapiDetailResponse;
import com.xavi.swapi.dto.swapi.SwapiListResponse;
import com.xavi.swapi.dto.swapi.SwapiResultItem;
import com.xavi.swapi.dto.swapi.VehicleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final SwapiClient swapiClient;

    public PageResponse<VehicleListItem> getAll(int page, int limit, String name) {
        SwapiListResponse<SwapiResultItem> response;

        if (name != null && !name.isBlank()) {
            response = swapiClient.searchVehiclesByName(name, page, limit);
        } else {
            response = swapiClient.getVehicles(page, limit);
        }

        List<VehicleListItem> vehicles = response.getResults() != null
                ? response.getResults().stream()
                    .map(item -> VehicleListItem.builder()
                            .uid(item.getUid())
                            .name(item.getName())
                            .url(item.getUrl())
                            .build())
                    .collect(Collectors.toList())
                : List.of();

        return PageResponse.<VehicleListItem>builder()
                .content(vehicles)
                .page(page)
                .limit(limit)
                .totalRecords(response.getTotalRecords())
                .totalPages(response.getTotalPages())
                .hasNext(response.getNext() != null)
                .hasPrevious(response.getPrevious() != null)
                .build();
    }

    public VehicleDTO getById(String id) {
        SwapiDetailResponse<VehicleProperties> response = swapiClient.getVehicleById(id);
        VehicleProperties props = response.getResult().getProperties();

        return VehicleDTO.builder()
                .uid(response.getResult().getUid())
                .name(props.getName())
                .model(props.getModel())
                .manufacturer(props.getManufacturer())
                .vehicleClass(props.getVehicleClass())
                .length(props.getLength())
                .crew(props.getCrew())
                .passengers(props.getPassengers())
                .cargoCapacity(props.getCargoCapacity())
                .costInCredits(props.getCostInCredits())
                .maxAtmospheringSpeed(props.getMaxAtmospheringSpeed())
                .consumables(props.getConsumables())
                .films(props.getFilms())
                .pilots(props.getPilots())
                .url(props.getUrl())
                .created(props.getCreated())
                .edited(props.getEdited())
                .build();
    }
}
