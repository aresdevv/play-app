package com.platzi.play.domain.dto;

import com.platzi.play.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta de autenticación con token JWT")
public record AuthResponseDto(
        @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,
        
        @Schema(description = "Tipo de token", example = "Bearer")
        String tokenType,
        
        @Schema(description = "Tiempo de expiración del token en segundos", example = "3600")
        Long expiresIn,
        
        @Schema(description = "Información del usuario autenticado")
        UserInfoDto user
) {
    
    @Schema(description = "Información básica del usuario")
    public record UserInfoDto(
            @Schema(description = "ID del usuario", example = "1")
            Long id,
            
            @Schema(description = "Nombre de usuario", example = "johndoe")
            String username,
            
            @Schema(description = "Email del usuario", example = "john@example.com")
            String email,
            
            @Schema(description = "Nombre completo", example = "John Doe")
            String nombreCompleto,
            
            @Schema(description = "Rol del usuario", example = "USER")
            UserEntity.UserRole role,
            
            @Schema(description = "Fecha de último acceso", example = "2024-01-20T15:45:00")
            LocalDateTime ultimoAcceso
    ) {}
}
