package com.xavi.swapi.controller;

import com.xavi.swapi.dto.PageResponse;
import com.xavi.swapi.dto.VehicleDTO;
import com.xavi.swapi.dto.VehicleListItem;
import com.xavi.swapi.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Endpoints para vehículos de Star Wars")
@SecurityRequirement(name = "Bearer Authentication")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Listar vehículos", description = "Obtiene lista paginada de vehículos con filtro opcional por nombre")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<VehicleListItem>> getAll(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Cantidad por página") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Filtrar por nombre") @RequestParam(required = false) String name) {
        return ResponseEntity.ok(vehicleService.getAll(page, limit, name));
    }

    @Operation(summary = "Obtener vehículo por ID", description = "Obtiene los detalles completos de un vehículo")
    @ApiResponse(responseCode = "200", description = "Vehículo encontrado")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(
            @Parameter(description = "ID del vehículo") @PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }
}
