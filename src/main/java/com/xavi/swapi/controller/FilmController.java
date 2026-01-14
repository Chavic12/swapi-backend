package com.xavi.swapi.controller;

import com.xavi.swapi.dto.FilmDTO;
import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/films")
@RequiredArgsConstructor
@Tag(name = "Films", description = "Endpoints para películas de Star Wars")
@SecurityRequirement(name = "Bearer Authentication")
public class FilmController {

    private final FilmService filmService;

    @Operation(summary = "Listar películas", description = "Obtiene lista paginada de películas con todos los datos")
    @ApiResponse(responseCode = "200", description = "Lista de películas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<FilmDTO>> getAll(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad por página") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filtrar por título") @RequestParam(required = false) String title) {
        return ResponseEntity.ok(filmService.getAll(page, limit, title));
    }

    @Operation(summary = "Obtener película por ID", description = "Obtiene los detalles completos de una película")
    @ApiResponse(responseCode = "200", description = "Película encontrada")
    @ApiResponse(responseCode = "404", description = "Película no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getById(
            @Parameter(description = "ID de la película") @PathVariable String id) {
        return ResponseEntity.ok(filmService.getById(id));
    }
}
