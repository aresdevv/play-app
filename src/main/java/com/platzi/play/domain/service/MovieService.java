package com.platzi.play.domain.service;

import com.platzi.play.domain.Genre;
import com.platzi.play.domain.dto.MovieDto;
import com.platzi.play.domain.dto.TmdbMovieDetailsDto;
import com.platzi.play.domain.dto.UpdateMovieDto;
import com.platzi.play.domain.exception.MovieAlreadyException;
import com.platzi.play.domain.repository.MovieRepository;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final TmdbService tmdbService;

    public MovieService(MovieRepository movieRepository, TmdbService tmdbService) {
        this.movieRepository = movieRepository;
        this.tmdbService = tmdbService;
    }

    @Tool("Busca todas las peliculas que existan dentro de la plataforma")
    public List<MovieDto> getAll(){
        return this.movieRepository.getAll();
    }

    public MovieDto getById(Long id){
        return this.movieRepository.getById(id);
    }

    public MovieDto save(MovieDto movieDto){
        return this.movieRepository.save(movieDto);
    }

    public MovieDto update(Long id, UpdateMovieDto updateMovieDto){
        return this.movieRepository.update(id,updateMovieDto);
    }

    public void delete(Long id){
        this.movieRepository.delete(id);
    }

    public MovieDto importFromTmdb(Long tmdbId) {
        Optional<MovieDto> existingMovie = findByTmdbId(tmdbId);
        if (existingMovie.isPresent()) {
            return existingMovie.get();
        }

        TmdbMovieDetailsDto tmdbDetails = tmdbService.getMovieDetails(tmdbId);

        MovieDto movieToSave = mapTmdbToMovieDto(tmdbDetails);

        return this.movieRepository.save(movieToSave);
    }

    private Optional<MovieDto> findByTmdbId(Long tmdbId) {
        return this.movieRepository.findByTmdbId(tmdbId);
    }

    private MovieDto mapTmdbToMovieDto(TmdbMovieDetailsDto tmdbDetails) {
        Genre genre = Genre.OTHER;
        if (tmdbDetails.getGenres() != null && !tmdbDetails.getGenres().isEmpty()) {
            genre = mapTmdbGenreToOurGenre(tmdbDetails.getGenres().get(0).getName());
        }

        LocalDate releaseDate = null;
        if (tmdbDetails.getReleaseDate() != null && !tmdbDetails.getReleaseDate().isEmpty()) {
            try {
                releaseDate = LocalDate.parse(tmdbDetails.getReleaseDate(), DateTimeFormatter.ISO_DATE);
            } catch (Exception e) {
            }
        }

        Double rating = tmdbDetails.getVoteAverage();

        return new MovieDto(
                null,
                tmdbDetails.getTitle(),
                tmdbDetails.getRuntime() != null ? tmdbDetails.getRuntime() : 0,
                genre,
                releaseDate,
                rating,
                true,
                null,
                0L,
                tmdbDetails.getId(),
                tmdbDetails.getPosterUrl(),
                tmdbDetails.getBackdropUrl(),
                tmdbDetails.getOverview(),
                tmdbDetails.getOriginalTitle(),
                tmdbDetails.getVoteAverage(),
                tmdbDetails.getVoteCount(),
                tmdbDetails.getPopularity(),
                null
        );
    }

    private Genre mapTmdbGenreToOurGenre(String tmdbGenre) {
        if (tmdbGenre == null) return Genre.OTHER;

        return switch (tmdbGenre.toLowerCase()) {
            case "acción" -> Genre.ACTION;
            case "comedia" -> Genre.COMEDY;
            case "drama" -> Genre.DRAMA;
            case "terror" -> Genre.HORROR;
            case "ciencia ficción" -> Genre.SCI_FI;
            case "suspense" -> Genre.THRILLER;
            case "romance" -> Genre.ROMANCE;
            case "animación" -> Genre.ANIMATED;
            case "aventura" -> Genre.ADVENTURE;
            case "fantasía" -> Genre.FANTASY;
            case "misterio" -> Genre.MYSTERY;
            case "crimen" -> Genre.CRIME;
            case "documental" -> Genre.DOCUMENTARY;
            default -> Genre.OTHER;
        };
    }
}
