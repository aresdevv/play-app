package com.platzi.play.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Schema(description = "Datos para actualizar una película existente")
public record UpdateMovieDto(
        @Schema(description = "Nuevo título de la película", example = "El Padrino - Edición Especial", required = true)
        @NotBlank(message = "El titulo es obligatorio")
        String title,

        @Schema(description = "Nueva fecha de estreno de la película", example = "1972-03-24")
        @PastOrPresent(message = "La fecha de lanzamiento debe ser anterior a la fecha actual")
        LocalDate releaseDate,

        @Schema(description = "Nueva calificación de la película (0-10)", example = "9.5")
        @Min(value = 0, message = "El rating no puede ser menor que 0")
        @Max(value = 10, message = "El rating no puede ser mayor que 10")
        Double rating
) {
}
