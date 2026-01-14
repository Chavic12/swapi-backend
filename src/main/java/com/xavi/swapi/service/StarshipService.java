package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.StarshipDTO;
import com.xavi.swapi.dto.StarshipListItem;
import com.xavi.swapi.dto.swapi.SwapiDetailResponse;
import com.xavi.swapi.dto.swapi.SwapiListResponse;
import com.xavi.swapi.dto.swapi.SwapiResultItem;
import com.xavi.swapi.dto.swapi.StarshipProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StarshipService {

    private final SwapiClient swapiClient;

    public PageResponse<StarshipListItem> getAll(int page, int limit, String name) {
        SwapiListResponse<SwapiResultItem> response;

        if (name != null && !name.isBlank()) {
            response = swapiClient.searchStarshipsByName(name, page, limit);
        } else {
            response = swapiClient.getStarships(page, limit);
        }

        List<StarshipListItem> starships = response.getResults() != null
                ? response.getResults().stream()
                    .map(item -> StarshipListItem.builder()
                            .uid(item.getUid())
                            .name(item.getName())
                            .url(item.getUrl())
                            .build())
                    .collect(Collectors.toList())
                : List.of();

        return PageResponse.<StarshipListItem>builder()
                .content(starships)
                .page(page)
                .limit(limit)
                .totalRecords(response.getTotalRecords())
                .totalPages(response.getTotalPages())
                .hasNext(response.getNext() != null)
                .hasPrevious(response.getPrevious() != null)
                .build();
    }

    public StarshipDTO getById(String id) {
        SwapiDetailResponse<StarshipProperties> response = swapiClient.getStarshipById(id);
        StarshipProperties props = response.getResult().getProperties();

        return StarshipDTO.builder()
                .uid(response.getResult().getUid())
                .name(props.getName())
                .model(props.getModel())
                .manufacturer(props.getManufacturer())
                .starshipClass(props.getStarshipClass())
                .length(props.getLength())
                .crew(props.getCrew())
                .passengers(props.getPassengers())
                .cargoCapacity(props.getCargoCapacity())
                .costInCredits(props.getCostInCredits())
                .maxAtmospheringSpeed(props.getMaxAtmospheringSpeed())
                .hyperdriveRating(props.getHyperdriveRating())
                .mglt(props.getMglt())
                .consumables(props.getConsumables())
                .films(props.getFilms())
                .pilots(props.getPilots())
                .url(props.getUrl())
                .created(props.getCreated())
                .edited(props.getEdited())
                .build();
    }
}
