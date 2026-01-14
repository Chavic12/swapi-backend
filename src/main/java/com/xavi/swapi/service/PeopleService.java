package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.PeopleDTO;
import com.xavi.swapi.dto.PeopleListItem;
import com.xavi.swapi.dto.swapi.PeopleProperties;
import com.xavi.swapi.dto.swapi.SwapiDetailResponse;
import com.xavi.swapi.dto.swapi.SwapiListResponse;
import com.xavi.swapi.dto.swapi.SwapiResultItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final SwapiClient swapiClient;

    public PageResponse<PeopleListItem> getAll(int page, int limit, String name) {
        SwapiListResponse<SwapiResultItem> response;

        if (name != null && !name.isBlank()) {
            response = swapiClient.searchPeopleByName(name, page, limit);
        } else {
            response = swapiClient.getPeople(page, limit);
        }

        List<PeopleListItem> people = response.getResults() != null
                ? response.getResults().stream()
                    .map(item -> PeopleListItem.builder()
                            .uid(item.getUid())
                            .name(item.getName())
                            .url(item.getUrl())
                            .build())
                    .collect(Collectors.toList())
                : List.of();

        return PageResponse.<PeopleListItem>builder()
                .content(people)
                .page(page)
                .limit(limit)
                .totalRecords(response.getTotalRecords())
                .totalPages(response.getTotalPages())
                .hasNext(response.getNext() != null)
                .hasPrevious(response.getPrevious() != null)
                .build();
    }

    public PeopleDTO getById(String id) {
        SwapiDetailResponse<PeopleProperties> response = swapiClient.getPeopleById(id);
        PeopleProperties props = response.getResult().getProperties();

        return PeopleDTO.builder()
                .uid(response.getResult().getUid())
                .name(props.getName())
                .gender(props.getGender())
                .skinColor(props.getSkinColor())
                .hairColor(props.getHairColor())
                .height(props.getHeight())
                .eyeColor(props.getEyeColor())
                .mass(props.getMass())
                .birthYear(props.getBirthYear())
                .homeworld(props.getHomeworld())
                .films(props.getFilms())
                .vehicles(props.getVehicles())
                .starships(props.getStarships())
                .url(props.getUrl())
                .created(props.getCreated())
                .edited(props.getEdited())
                .build();
    }
}
