package com.xavi.swapi.controller;

import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.StarshipDTO;
import com.xavi.swapi.dto.StarshipListItem;
import com.xavi.swapi.service.StarshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/starships")
@RequiredArgsConstructor
@Tag(name = "Starships", description = "Endpoints para naves espaciales de Star Wars")
@SecurityRequirement(name = "Bearer Authentication")
public class StarshipController {

    private final StarshipService starshipService;

    @Operation(summary = "Listar naves", description = "Obtiene lista paginada de naves con filtro opcional por nombre")
    @ApiResponse(responseCode = "200", description = "Lista de naves obtenida exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<StarshipListItem>> getAll(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad por página") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filtrar por nombre") @RequestParam(required = false) String name) {
        return ResponseEntity.ok(starshipService.getAll(page, limit, name));
    }

    @Operation(summary = "Obtener nave por ID", description = "Obtiene los detalles completos de una nave")
    @ApiResponse(responseCode = "200", description = "Nave encontrada")
    @ApiResponse(responseCode = "404", description = "Nave no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<StarshipDTO> getById(
            @Parameter(description = "ID de la nave") @PathVariable String id) {
        return ResponseEntity.ok(starshipService.getById(id));
    }
}
