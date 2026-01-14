package com.xavi.swapi.controller;

import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.PeopleDTO;
import com.xavi.swapi.dto.PeopleListItem;
import com.xavi.swapi.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
@Tag(name = "People", description = "Endpoints para personajes de Star Wars")
@SecurityRequirement(name = "Bearer Authentication")
public class PeopleController {

    private final PeopleService peopleService;

    @Operation(summary = "Listar personajes", description = "Obtiene lista paginada de personajes con filtro opcional por nombre")
    @ApiResponse(responseCode = "200", description = "Lista de personajes obtenida exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<PeopleListItem>> getAll(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad por página") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filtrar por nombre") @RequestParam(required = false) String name) {
        return ResponseEntity.ok(peopleService.getAll(page, limit, name));
    }

    @Operation(summary = "Obtener personaje por ID", description = "Obtiene los detalles completos de un personaje")
    @ApiResponse(responseCode = "200", description = "Personaje encontrado")
    @ApiResponse(responseCode = "404", description = "Personaje no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<PeopleDTO> getById(
            @Parameter(description = "ID del personaje") @PathVariable String id) {
        return ResponseEntity.ok(peopleService.getById(id));
    }
}
