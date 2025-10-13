package com.platzi.play.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * DTO para representar detalles completos de película desde TMDB API
 */
@Data
public class TmdbMovieDetailsDto {
    private Long id;
    private String title;
    
    @JsonProperty("original_title")
    private String originalTitle;
    
    private String overview;
    private String tagline;
    
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
    private Integer runtime;
    private Long budget;
    private Long revenue;
    private String status;
    
    private List<Genre> genres;
    
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;
    
    @JsonProperty("production_countries")
    private List<ProductionCountry> productionCountries;
    
    @JsonProperty("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;
    
    private String homepage;
    
    @JsonProperty("imdb_id")
    private String imdbId;
    
    // URLs completas (se construirán en el servicio)
    private String posterUrl;
    private String backdropUrl;
    
    @Data
    public static class Genre {
        private Integer id;
        private String name;
    }
    
    @Data
    public static class ProductionCompany {
        private Integer id;
        private String name;
        
        @JsonProperty("logo_path")
        private String logoPath;
        
        @JsonProperty("origin_country")
        private String originCountry;
    }
    
    @Data
    public static class ProductionCountry {
        @JsonProperty("iso_3166_1")
        private String iso31661;
        private String name;
    }
    
    @Data
    public static class SpokenLanguage {
        @JsonProperty("iso_639_1")
        private String iso6391;
        private String name;
        
        @JsonProperty("english_name")
        private String englishName;
    }
}

