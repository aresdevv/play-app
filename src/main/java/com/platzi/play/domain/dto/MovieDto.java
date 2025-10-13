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
        boolean available,
        
        @Schema(description = "Calificación promedio basada en reviews de usuarios (1-5)", example = "4.5")
        Double averageUserRating,
        
        @Schema(description = "Cantidad total de reviews de usuarios", example = "150")
        Long reviewCount,
        
        // ========== Campos de integración con TMDB ==========
        
        @Schema(description = "ID de la película en TMDB (si fue importada)", example = "603")
        Long tmdbId,
        
        @Schema(description = "URL del poster de la película", example = "https://image.tmdb.org/t/p/w500/...")
        String posterUrl,
        
        @Schema(description = "URL del backdrop de la película", example = "https://image.tmdb.org/t/p/original/...")
        String backdropUrl,
        
        @Schema(description = "Sinopsis de la película")
        String overview,
        
        @Schema(description = "Título original de la película", example = "The Godfather")
        String originalTitle,
        
        @Schema(description = "Calificación promedio en TMDB (0-10)", example = "8.7")
        Double voteAverage,
        
        @Schema(description = "Cantidad de votos en TMDB", example = "25000")
        Integer voteCount,
        
        @Schema(description = "Popularidad en TMDB", example = "65.23")
        Double popularity,
        
        @Schema(description = "Idioma original de la película", example = "en")
        String originalLanguage
) {
}
