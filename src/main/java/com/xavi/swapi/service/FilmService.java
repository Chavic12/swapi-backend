package com.xavi.swapi.service;

import com.xavi.swapi.client.SwapiClient;
import com.xavi.swapi.dto.FilmDTO;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.swapi.FilmProperties;
import com.xavi.swapi.dto.swapi.FilmResultItem;
import com.xavi.swapi.dto.swapi.SwapiDetailResponse;
import com.xavi.swapi.dto.swapi.SwapiListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final SwapiClient swapiClient;

    public PageResponse<FilmDTO> getAll(int page, int limit, String title) {
        SwapiListResponse<FilmResultItem> response;

        if (title != null && !title.isBlank()) {
            response = swapiClient.searchFilmsByName(title, page, limit);
        } else {
            response = swapiClient.getFilms(page, limit);
        }

        List<FilmDTO> films = response.getResults() != null
                ? response.getResults().stream()
                    .map(item -> {
                        FilmProperties props = item.getProperties();
                        return FilmDTO.builder()
                                .uid(item.getUid())
                                .title(props.getTitle())
                                .episodeId(props.getEpisodeId())
                                .director(props.getDirector())
                                .producer(props.getProducer())
                                .releaseDate(props.getReleaseDate())
                                .openingCrawl(props.getOpeningCrawl())
                                .characters(props.getCharacters())
                                .starships(props.getStarships())
                                .vehicles(props.getVehicles())
                                .planets(props.getPlanets())
                                .species(props.getSpecies())
                                .url(props.getUrl())
                                .created(props.getCreated())
                                .edited(props.getEdited())
                                .build();
                    })
                    .collect(Collectors.toList())
                : List.of();

        return PageResponse.<FilmDTO>builder()
                .content(films)
                .page(page)
                .limit(limit)
                .totalRecords(response.getTotalRecords())
                .totalPages(response.getTotalPages())
                .hasNext(response.getNext() != null)
                .hasPrevious(response.getPrevious() != null)
                .build();
    }

    public FilmDTO getById(String id) {
        SwapiDetailResponse<FilmProperties> response = swapiClient.getFilmById(id);
        FilmProperties props = response.getResult().getProperties();

        return FilmDTO.builder()
                .uid(response.getResult().getUid())
                .title(props.getTitle())
                .episodeId(props.getEpisodeId())
                .director(props.getDirector())
                .producer(props.getProducer())
                .releaseDate(props.getReleaseDate())
                .openingCrawl(props.getOpeningCrawl())
                .characters(props.getCharacters())
                .starships(props.getStarships())
                .vehicles(props.getVehicles())
                .planets(props.getPlanets())
                .species(props.getSpecies())
                .url(props.getUrl())
                .created(props.getCreated())
                .edited(props.getEdited())
                .build();
    }
}
