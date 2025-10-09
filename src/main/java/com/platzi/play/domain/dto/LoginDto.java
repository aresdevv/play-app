package com.platzi.play.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para inicio de sesión")
public record LoginDto(
        @Schema(description = "Nombre de usuario o email", example = "johndoe", required = true)
        @NotBlank(message = "El nombre de usuario o email es obligatorio")
        String usernameOrEmail,
        
        @Schema(description = "Contraseña del usuario", example = "password123", required = true)
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
