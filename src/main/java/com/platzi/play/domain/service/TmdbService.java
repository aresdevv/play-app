package com.platzi.play.domain.service;

import com.platzi.play.domain.dto.TmdbMovieDetailsDto;
import com.platzi.play.domain.dto.TmdbMovieDto;
import com.platzi.play.domain.dto.TmdbSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Servicio para consumir la API de The Movie Database (TMDB)
 */
@Service
public class TmdbService {
    
    private final RestTemplate restTemplate;
    
    @Value("${tmdb.api.key}")
    private String apiKey;
    
    @Value("${tmdb.api.base-url}")
    private String baseUrl;
    
    @Value("${tmdb.api.image-base-url}")
    private String imageBaseUrl;
    
    public TmdbService() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Busca películas por título en TMDB
     * 
     * @param query Título de la película a buscar
     * @param page Número de página (opcional, por defecto 1)
     * @return Lista de películas encontradas
     */
    public TmdbSearchResponseDto searchMovies(String query, Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .queryParam("page", page)
                .queryParam("language", "es-MX")
                .toUriString();
        
        TmdbSearchResponseDto response = restTemplate.getForObject(url, TmdbSearchResponseDto.class);
        
        if (response != null && response.getResults() != null) {
            response.getResults().forEach(this::setImageUrls);
        }
        
        return response;
    }
    
    /**
     * Obtiene detalles completos de una película por su ID de TMDB
     * 
     * @param tmdbId ID de la película en TMDB
     * @return Detalles completos de la película
     */
    public TmdbMovieDetailsDto getMovieDetails(Long tmdbId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/" + tmdbId)
                .queryParam("api_key", apiKey)
                .queryParam("language", "es-MX")
                .toUriString();
        
        TmdbMovieDetailsDto details = restTemplate.getForObject(url, TmdbMovieDetailsDto.class);
        
        if (details != null) {
            setDetailImageUrls(details);
        }
        
        return details;
    }
    
    /**
     * Obtiene películas populares del momento
     * 
     * @param page Número de página
     * @return Lista de películas populares
     */
    public TmdbSearchResponseDto getPopularMovies(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/popular")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .queryParam("language", "es-MX")
                .toUriString();
        
        TmdbSearchResponseDto response = restTemplate.getForObject(url, TmdbSearchResponseDto.class);
        
        if (response != null && response.getResults() != null) {
            response.getResults().forEach(this::setImageUrls);
        }
        
        return response;
    }
    
    /**
     * Obtiene películas mejor calificadas
     * 
     * @param page Número de página
     * @return Lista de películas mejor calificadas
     */
    public TmdbSearchResponseDto getTopRatedMovies(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/top_rated")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .queryParam("language", "es-MX")
                .toUriString();
        
        TmdbSearchResponseDto response = restTemplate.getForObject(url, TmdbSearchResponseDto.class);
        
        if (response != null && response.getResults() != null) {
            response.getResults().forEach(this::setImageUrls);
        }
        
        return response;
    }
    
    /**
     * Obtiene películas que están en cines actualmente
     * 
     * @param page Número de página
     * @return Lista de películas en cines
     */
    public TmdbSearchResponseDto getNowPlayingMovies(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/now_playing")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .queryParam("language", "es-MX")
                .queryParam("region", "MX")
                .toUriString();
        
        TmdbSearchResponseDto response = restTemplate.getForObject(url, TmdbSearchResponseDto.class);
        
        if (response != null && response.getResults() != null) {
            response.getResults().forEach(this::setImageUrls);
        }
        
        return response;
    }
    
    /**
     * Obtiene películas por próximas a estrenarse
     * 
     * @param page Número de página
     * @return Lista de películas próximas
     */
    public TmdbSearchResponseDto getUpcomingMovies(Integer page) {
        if (page == null || page < 1) {
            page = 1;
        }
        
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/movie/upcoming")
                .queryParam("api_key", apiKey)
                .queryParam("page", page)
                .queryParam("language", "es-MX")
                .queryParam("region", "MX")
                .toUriString();
        
        TmdbSearchResponseDto response = restTemplate.getForObject(url, TmdbSearchResponseDto.class);
        
        if (response != null && response.getResults() != null) {
            response.getResults().forEach(this::setImageUrls);
        }
        
        return response;
    }
    
    /**
     * Construye URLs completas para las imágenes de una película
     */
    private void setImageUrls(TmdbMovieDto movie) {
        if (movie.getPosterPath() != null) {
            movie.setPosterUrl(imageBaseUrl + "/w500" + movie.getPosterPath());
        }
        if (movie.getBackdropPath() != null) {
            movie.setBackdropUrl(imageBaseUrl + "/original" + movie.getBackdropPath());
        }
    }
    
    /**
     * Construye URLs completas para las imágenes de detalles de película
     */
    private void setDetailImageUrls(TmdbMovieDetailsDto details) {
        if (details.getPosterPath() != null) {
            details.setPosterUrl(imageBaseUrl + "/w500" + details.getPosterPath());
        }
        if (details.getBackdropPath() != null) {
            details.setBackdropUrl(imageBaseUrl + "/original" + details.getBackdropPath());
        }
    }
}

