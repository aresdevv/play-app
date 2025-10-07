package com.platzi.play.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SuggestRequestDto(
     @NotBlank(message = "Las preferencias del usuario son obligatorias")
     @Size(min = 10, max = 500, message = "Las preferencias deben tener entre 10 y 500 caracteres")
     String userPreferences
) {
}
