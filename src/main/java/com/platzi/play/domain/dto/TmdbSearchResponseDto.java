package com.platzi.play.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO para representar la respuesta de búsqueda de TMDB API
 */
@Data
public class TmdbSearchResponseDto {
    private Integer page;
    
    @JsonProperty("total_results")
    private Integer totalResults;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    private List<TmdbMovieDto> results;
}

