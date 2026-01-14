package com.xavi.swapi.controller;

import com.xavi.swapi.dto.FavoriteRequest;
import com.xavi.swapi.dto.FavoriteResponse;
import com.xavi.swapi.dto.MessageResponse;
import com.xavi.swapi.entity.ResourceType;
import com.xavi.swapi.entity.User;
import com.xavi.swapi.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "Gestionar favoritos de recursos: PEOPLE, FILMS, STARSHIPS, VEHICLES")
@SecurityRequirement(name = "Bearer Authentication")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Agregar favorito", description = "Agrega un recurso a favoritos. Tipos: PEOPLE, FILMS, STARSHIPS, VEHICLES")
    @ApiResponse(responseCode = "201", description = "Favorito agregado exitosamente")
    @ApiResponse(responseCode = "409", description = "El recurso ya esta en favoritos")
    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.addFavorite(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar favoritos", description = "Lista favoritos del usuario. Filtro opcional: PEOPLE, FILMS, STARSHIPS, VEHICLES")
    @ApiResponse(responseCode = "200", description = "Lista de favoritos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
            @AuthenticationPrincipal User user,
            @Parameter(description = "Filtrar por tipo: PEOPLE, FILMS, STARSHIPS, VEHICLES") @RequestParam(required = false) ResourceType type) {
        return ResponseEntity.ok(favoriteService.getFavorites(user, type));
    }

    @Operation(summary = "Eliminar favorito", description = "Elimina un recurso de los favoritos del usuario")
    @ApiResponse(responseCode = "200", description = "Favorito eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Favorito no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> removeFavorite(
            @AuthenticationPrincipal User user,
            @Parameter(description = "ID del favorito") @PathVariable Long id) {
        favoriteService.removeFavorite(user, id);
        return ResponseEntity.ok(new MessageResponse("Favorito eliminado exitosamente"));
    }

    @Operation(summary = "Verificar favorito", description = "Verifica si un recurso esta en los favoritos del usuario")
    @ApiResponse(responseCode = "200", description = "Estado del favorito")
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFavorite(
            @AuthenticationPrincipal User user,
            @Parameter(description = "Tipo: PEOPLE, FILMS, STARSHIPS, VEHICLES") @RequestParam ResourceType type,
            @Parameter(description = "ID del recurso") @RequestParam String resourceId) {
        return ResponseEntity.ok(favoriteService.isFavorite(user, type, resourceId));
    }
}
