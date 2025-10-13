package com.platzi.play.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO para representar información de película desde TMDB API
 */
@Data
public class TmdbMovieDto {
    private Long id;
    private String title;
    
    @JsonProperty("original_title")
    private String originalTitle;
    
    private String overview;
    
    @JsonProperty("poster_path")
    private String posterPath;
    
    @JsonProperty("backdrop_path")
    private String backdropPath;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("vote_average")
    private Double voteAverage;
    
    @JsonProperty("vote_count")
    private Integer voteCount;
    
    private Double popularity;
    
    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
    
    private Boolean adult;
    
    @JsonProperty("original_language")
    private String originalLanguage;
    
    // URLs completas (se construirán en el servicio)
    private String posterUrl;
    private String backdropUrl;
}

