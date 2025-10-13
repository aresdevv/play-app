package com.platzi.play.web.controller;

import com.platzi.play.domain.dto.TmdbMovieDetailsDto;
import com.platzi.play.domain.dto.TmdbSearchResponseDto;
import com.platzi.play.domain.service.TmdbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para endpoints de integración con TMDB (The Movie Database)
 */
@RestController
@RequestMapping("/tmdb")
@Tag(name = "TMDB", description = "Endpoints para consumir información de The Movie Database")
public class TmdbController {
    
    private final TmdbService tmdbService;
    
    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }
    
    @GetMapping("/search")
    @Operation(
        summary = "Buscar películas en TMDB",
        description = "Busca películas por título en The Movie Database"
    )
    public ResponseEntity<TmdbSearchResponseDto> searchMovies(
            @Parameter(description = "Título de la película a buscar")
            @RequestParam String query,
            @Parameter(description = "Número de página (opcional, por defecto 1)")
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        TmdbSearchResponseDto response = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/movie/{tmdbId}")
    @Operation(
        summary = "Obtener detalles de película",
        description = "Obtiene información detallada de una película por su ID de TMDB"
    )
    public ResponseEntity<TmdbMovieDetailsDto> getMovieDetails(
            @Parameter(description = "ID de la película en TMDB")
            @PathVariable Long tmdbId
    ) {
        TmdbMovieDetailsDto details = tmdbService.getMovieDetails(tmdbId);
        return ResponseEntity.ok(details);
    }
    
    @GetMapping("/popular")
    @Operation(
        summary = "Películas populares",
        description = "Obtiene las películas más populares del momento según TMDB"
    )
    public ResponseEntity<TmdbSearchResponseDto> getPopularMovies(
            @Parameter(description = "Número de página (opcional, por defecto 1)")
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        TmdbSearchResponseDto response = tmdbService.getPopularMovies(page);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/top-rated")
    @Operation(
        summary = "Películas mejor calificadas",
        description = "Obtiene las películas mejor calificadas según TMDB"
    )
    public ResponseEntity<TmdbSearchResponseDto> getTopRatedMovies(
            @Parameter(description = "Número de página (opcional, por defecto 1)")
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        TmdbSearchResponseDto response = tmdbService.getTopRatedMovies(page);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/now-playing")
    @Operation(
        summary = "Películas en cines",
        description = "Obtiene las películas que están actualmente en cines"
    )
    public ResponseEntity<TmdbSearchResponseDto> getNowPlayingMovies(
            @Parameter(description = "Número de página (opcional, por defecto 1)")
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        TmdbSearchResponseDto response = tmdbService.getNowPlayingMovies(page);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/upcoming")
    @Operation(
        summary = "Próximos estrenos",
        description = "Obtiene las películas que se estrenarán próximamente"
    )
    public ResponseEntity<TmdbSearchResponseDto> getUpcomingMovies(
            @Parameter(description = "Número de página (opcional, por defecto 1)")
            @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        TmdbSearchResponseDto response = tmdbService.getUpcomingMovies(page);
        return ResponseEntity.ok(response);
    }
}

