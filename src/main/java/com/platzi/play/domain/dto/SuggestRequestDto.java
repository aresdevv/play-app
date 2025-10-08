package com.platzi.play.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud para generar sugerencias de películas usando IA")
public record SuggestRequestDto(
     @Schema(description = "Preferencias del usuario para generar recomendaciones personalizadas", 
              example = "Me gustan las películas de acción y ciencia ficción, especialmente las de Marvel y DC", 
              required = true)
     @NotBlank(message = "Las preferencias del usuario son obligatorias")
     @Size(min = 10, max = 500, message = "Las preferencias deben tener entre 10 y 500 caracteres")
     String userPreferences
) {
}
