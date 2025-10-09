package com.platzi.play.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para registro de nuevo usuario")
public record RegisterDto(
        @Schema(description = "Nombre de usuario único", example = "johndoe", required = true)
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos")
        String username,
        
        @Schema(description = "Email del usuario", example = "john@example.com", required = true)
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede tener más de 150 caracteres")
        String email,
        
        @Schema(description = "Contraseña del usuario", example = "password123", required = true)
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
        String password,
        
        @Schema(description = "Confirmación de contraseña", example = "password123", required = true)
        @NotBlank(message = "La confirmación de contraseña es obligatoria")
        String confirmPassword,
        
        @Schema(description = "Nombre completo del usuario", example = "John Doe")
        @Size(max = 150, message = "El nombre completo no puede tener más de 150 caracteres")
        String nombreCompleto
) {
}
