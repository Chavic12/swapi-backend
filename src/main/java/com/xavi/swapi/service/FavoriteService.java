package com.xavi.swapi.service;

import com.xavi.swapi.dto.FavoriteRequest;
import com.xavi.swapi.dto.FavoriteResponse;
import com.xavi.swapi.entity.Favorite;
import com.xavi.swapi.entity.ResourceType;
import com.xavi.swapi.entity.User;
import com.xavi.swapi.exception.FavoriteAlreadyExistsException;
import com.xavi.swapi.exception.FavoriteNotFoundException;
import com.xavi.swapi.exception.ResourceNotFoundException;
import com.xavi.swapi.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PeopleService peopleService;
    private final FilmService filmService;
    private final StarshipService starshipService;
    private final VehicleService vehicleService;

    @Transactional
    public FavoriteResponse addFavorite(User user, FavoriteRequest request) {
        // Validar que el recurso existe en SWAPI
        Object resource = validateAndFetchResource(request.getResourceType(), request.getResourceId());

        if (favoriteRepository.existsByUserAndResourceTypeAndResourceId(
                user, request.getResourceType(), request.getResourceId())) {
            throw new FavoriteAlreadyExistsException("Este recurso ya esta en tus favoritos");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .resourceType(request.getResourceType())
                .resourceId(request.getResourceId())
                .build();

        Favorite saved = favoriteRepository.save(favorite);
        return mapToResponseWithResource(saved, resource);
    }

    public List<FavoriteResponse> getFavorites(User user, ResourceType resourceType) {
        List<Favorite> favorites;

        if (resourceType != null) {
            favorites = favoriteRepository.findByUserAndResourceTypeOrderByCreatedAtDesc(user, resourceType);
        } else {
            favorites = favoriteRepository.findByUserOrderByCreatedAtDesc(user);
        }

        return favorites.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(User user, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorito no encontrado"));

        if (!favorite.getUser().getId().equals(user.getId())) {
            throw new FavoriteNotFoundException("Favorito no encontrado");
        }

        favoriteRepository.delete(favorite);
    }

    public boolean isFavorite(User user, ResourceType resourceType, String resourceId) {
        return favoriteRepository.existsByUserAndResourceTypeAndResourceId(user, resourceType, resourceId);
    }

    private FavoriteResponse mapToResponse(Favorite favorite) {
        Object resource = fetchResource(favorite.getResourceType(), favorite.getResourceId());

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .resourceType(favorite.getResourceType())
                .resourceId(favorite.getResourceId())
                .createdAt(favorite.getCreatedAt())
                .resource(resource)
                .build();
    }

    private Object fetchResource(ResourceType type, String resourceId) {
        try {
            return switch (type) {
                case PEOPLE -> peopleService.getById(resourceId);
                case FILMS -> filmService.getById(resourceId);
                case STARSHIPS -> starshipService.getById(resourceId);
                case VEHICLES -> vehicleService.getById(resourceId);
            };
        } catch (Exception e) {
            return null;
        }
    }

    private Object validateAndFetchResource(ResourceType type, String resourceId) {
        try {
            return switch (type) {
                case PEOPLE -> peopleService.getById(resourceId);
                case FILMS -> filmService.getById(resourceId);
                case STARSHIPS -> starshipService.getById(resourceId);
                case VEHICLES -> vehicleService.getById(resourceId);
            };
        } catch (Exception e) {
            throw new ResourceNotFoundException(
                    String.format("Recurso %s con ID %s no encontrado en SWAPI", type, resourceId));
        }
    }

    private FavoriteResponse mapToResponseWithResource(Favorite favorite, Object resource) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .resourceType(favorite.getResourceType())
                .resourceId(favorite.getResourceId())
                .createdAt(favorite.getCreatedAt())
                .resource(resource)
                .build();
    }
}
