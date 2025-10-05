package com.platzi.play.persistence;

import com.platzi.play.domain.dto.MovieDto;
import com.platzi.play.domain.repository.MovieRepository;
import com.platzi.play.persistence.crud.CrudMovieEntity;
import com.platzi.play.persistence.entity.MovieEntity;
import com.platzi.play.persistence.mapper.MovieMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieEntityRepository implements MovieRepository {

    private final CrudMovieEntity crudMovieEntity;
    private final MovieMapper movieMapper;

    public MovieEntityRepository(CrudMovieEntity crudMovieEntity, MovieMapper movieMapper) {
        this.crudMovieEntity = crudMovieEntity;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> getAll() {
        return movieMapper.toDtoList(crudMovieEntity.findAll());
    }

    @Override
    public MovieDto getById(Long id) {
        MovieEntity movieEntity = crudMovieEntity.findById(id).orElse(null);
        return movieMapper.toDto(movieEntity);
    }

    @Override
    public MovieDto save(MovieDto movieDto) {

        MovieEntity movieEntity = movieMapper.toEntity(movieDto);
        movieEntity.setEstado("D");
        return this.movieMapper.toDto(crudMovieEntity.save(movieEntity));
    }
}
