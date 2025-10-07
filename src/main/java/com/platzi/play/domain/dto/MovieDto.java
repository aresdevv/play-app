package com.platzi.play.domain.dto;

import com.platzi.play.domain.Genre;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MovieDto(
        Long id,
        
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
        String title,
        
        @NotNull(message = "La duración es obligatoria")
        @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
        @Max(value = 600, message = "La duración no puede ser mayor a 600 minutos")
        Integer duration,
        
        @NotNull(message = "El género es obligatorio")
        Genre genre,
        
        @PastOrPresent(message = "La fecha de estreno debe ser anterior o igual a la fecha actual")
        LocalDate releaseDate,
        
        @Min(value = 0, message = "El rating no puede ser menor que 0")
        @Max(value = 10, message = "El rating no puede ser mayor que 10")
        Double rating,
        
        boolean available
) {
}
