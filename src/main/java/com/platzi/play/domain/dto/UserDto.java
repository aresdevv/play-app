package com.platzi.play.domain.dto;

import com.platzi.play.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representa un usuario en el sistema")
public record UserDto(
        @Schema(description = "Identificador único del usuario", example = "1")
        Long id,
        
        @Schema(description = "Nombre de usuario único", example = "johndoe")
        String username,
        
        @Schema(description = "Email del usuario", example = "john@example.com")
        String email,
        
        @Schema(description = "Nombre completo del usuario", example = "John Doe")
        String nombreCompleto,
        
        @Schema(description = "Rol del usuario", example = "USER")
        UserEntity.UserRole role,
        
        @Schema(description = "Fecha de creación de la cuenta", example = "2024-01-15T10:30:00")
        LocalDateTime fechaCreacion,
        
        @Schema(description = "Último acceso del usuario", example = "2024-01-20T15:45:00")
        LocalDateTime ultimoAcceso,
        
        @Schema(description = "Indica si la cuenta está activa", example = "true")
        Boolean activo,
        
        @Schema(description = "Indica si el email está verificado", example = "true")
        Boolean emailVerificado
) {
}
