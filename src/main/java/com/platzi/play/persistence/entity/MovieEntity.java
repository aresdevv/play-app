package com.platzi.play.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, precision = 3)
    private Integer duration;

    @Column(nullable = false, length = 40)
    private String genre;

    @Column(name="release_date")
    private LocalDate releaseDate;

    @Column(precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(nullable = false, length = 1)
    private String status;

    // ========== Campos de integración con TMDB ==========
    
    @Column(name = "tmdb_id", unique = true)
    private Long tmdbId;
    
    @Column(name = "poster_url", length = 500)
    private String posterUrl;
    
    @Column(name = "backdrop_url", length = 500)
    private String backdropUrl;
    
    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;
    
    @Column(name = "original_title", length = 200)
    private String originalTitle;
    
    @Column(name = "vote_average")
    private Double voteAverage;
    
    @Column(name = "vote_count")
    private Integer voteCount;
    
    @Column(name = "popularity")
    private Double popularity;
    
    @Column(name = "original_language", length = 10)
    private String originalLanguage;

}