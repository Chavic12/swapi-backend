package com.xavi.swapi.client;

import com.xavi.swapi.dto.swapi.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SwapiClient {

    private final WebClient swapiWebClient;

    // People
    public SwapiListResponse<SwapiResultItem> getPeople(int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/people")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiListResponse<SwapiResultItem> searchPeopleByName(String name, int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/people")
                        .queryParam("name", name)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiDetailResponse<PeopleProperties> getPeopleById(String id) {
        return swapiWebClient.get()
                .uri("/people/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiDetailResponse<PeopleProperties>>() {})
                .block();
    }

    // Films
    public SwapiListResponse<FilmResultItem> getFilms(int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/films")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<FilmResultItem>>() {})
                .block();
    }

    public SwapiListResponse<FilmResultItem> searchFilmsByName(String name, int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/films")
                        .queryParam("title", name)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<FilmResultItem>>() {})
                .block();
    }

    public SwapiDetailResponse<FilmProperties> getFilmById(String id) {
        return swapiWebClient.get()
                .uri("/films/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiDetailResponse<FilmProperties>>() {})
                .block();
    }

    // Starships
    public SwapiListResponse<SwapiResultItem> getStarships(int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/starships")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiListResponse<SwapiResultItem> searchStarshipsByName(String name, int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/starships")
                        .queryParam("name", name)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiDetailResponse<StarshipProperties> getStarshipById(String id) {
        return swapiWebClient.get()
                .uri("/starships/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiDetailResponse<StarshipProperties>>() {})
                .block();
    }

    // Vehicles
    public SwapiListResponse<SwapiResultItem> getVehicles(int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/vehicles")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiListResponse<SwapiResultItem> searchVehiclesByName(String name, int page, int limit) {
        return swapiWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/vehicles")
                        .queryParam("name", name)
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiListResponse<SwapiResultItem>>() {})
                .block();
    }

    public SwapiDetailResponse<VehicleProperties> getVehicleById(String id) {
        return swapiWebClient.get()
                .uri("/vehicles/{id}", id)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<SwapiDetailResponse<VehicleProperties>>() {})
                .block();
    }
}
