package com.xavi.swapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para inicio de sesión")
public class LoginRequest {
    @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "Password@123")
    private String password;
}
