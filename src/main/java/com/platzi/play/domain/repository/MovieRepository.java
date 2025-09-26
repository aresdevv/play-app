package com.platzi.play.domain.repository;

import com.platzi.play.domain.dto.MovieDto;

import java.util.List;


public interface MovieRepository {
    List<MovieDto> getAll();
    MovieDto getById(Long id);
}
