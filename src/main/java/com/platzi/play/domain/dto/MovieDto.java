package com.platzi.play.domain.dto;

import com.platzi.play.domain.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Representa una película en el sistema")
public record MovieDto(
        @Schema(description = "Identificador único de la película", example = "1")
        Long id,
        
        @Schema(description = "Título de la película", example = "El Padrino", required = true)
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
        String title,
        
        @Schema(description = "Duración de la película en minutos", example = "175", required = true)
        @NotNull(message = "La duración es obligatoria")
        @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
        @Max(value = 600, message = "La duración no puede ser mayor a 600 minutos")
        Integer duration,
        
        @Schema(description = "Género de la película", example = "DRAMA", required = true)
        @NotNull(message = "El género es obligatorio")
        Genre genre,
        
        @Schema(description = "Fecha de estreno de la película", example = "1972-03-24")
        @PastOrPresent(message = "La fecha de estreno debe ser anterior o igual a la fecha actual")
        LocalDate releaseDate,
        
        @Schema(description = "Calificación de la película (0-10)", example = "9.2")
        @Min(value = 0, message = "El rating no puede ser menor que 0")
        @Max(value = 10, message = "El rating no puede ser mayor que 10")
        Double rating,
        
        @Schema(description = "Indica si la película está disponible para visualización", example = "true")
        boolean available
) {
}
