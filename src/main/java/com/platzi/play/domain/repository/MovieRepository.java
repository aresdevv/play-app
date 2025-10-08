package com.platzi.play.domain.repository;

import com.platzi.play.domain.dto.MovieDto;
import com.platzi.play.domain.dto.UpdateMovieDto;

import java.util.List;


public interface MovieRepository {
    List<MovieDto> getAll();
    MovieDto getById(Long id);
    MovieDto save(MovieDto movieDto);
    MovieDto update(Long id,UpdateMovieDto movieDto);

    MovieDto update(UpdateMovieDto movieDto);
    void delete(Long id);
}