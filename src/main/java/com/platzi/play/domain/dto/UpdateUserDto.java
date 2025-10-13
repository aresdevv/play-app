package com.platzi.play.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para actualizar información de usuario")
public record UpdateUserDto(
        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        @Size(max = 150, message = "El nombre completo no puede exceder 150 caracteres")
        String nombreCompleto,

        @Schema(description = "Email del usuario", example = "juan.perez@email.com")
        @Email(message = "El email debe ser válido")
        @Size(max = 150, message = "El email no puede exceder 150 caracteres")
        String email
) {
}
