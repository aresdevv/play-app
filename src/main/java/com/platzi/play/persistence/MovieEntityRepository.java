package com.platzi.play.persistence;

import com.platzi.play.domain.dto.MovieDto;
import com.platzi.play.domain.dto.UpdateMovieDto;
import com.platzi.play.domain.exception.MovieAlreadyException;
import com.platzi.play.domain.exception.MovieNotFoundException;
import com.platzi.play.domain.exception.MovieTitleAlreadyExistsException;
import com.platzi.play.domain.repository.MovieRepository;
import com.platzi.play.persistence.crud.CrudMovieEntity;
import com.platzi.play.persistence.crud.CrudReviewEntity;
import com.platzi.play.persistence.entity.MovieEntity;
import com.platzi.play.persistence.mapper.MovieMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class MovieEntityRepository implements MovieRepository {

    private final CrudMovieEntity crudMovieEntity;
    private final CrudReviewEntity crudReviewEntity;
    private final MovieMapper movieMapper;

    public MovieEntityRepository(CrudMovieEntity crudMovieEntity, CrudReviewEntity crudReviewEntity, MovieMapper movieMapper) {
        this.crudMovieEntity = crudMovieEntity;
        this.crudReviewEntity = crudReviewEntity;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> getAll() {
        List<MovieDto> movies = movieMapper.toDtoList(crudMovieEntity.findAll());
        return enrichMoviesWithReviewData(movies);
    }

    @Override
    public MovieDto getById(Long id) {
        MovieEntity movieEntity = crudMovieEntity.findById(id).orElse(null);
        MovieDto movieDto = movieMapper.toDto(movieEntity);
        return enrichMovieWithReviewData(movieDto);
    }
    
    private List<MovieDto> enrichMoviesWithReviewData(List<MovieDto> movies) {
        return movies.stream()
                .map(this::enrichMovieWithReviewData)
                .toList();
    }
    
    private MovieDto enrichMovieWithReviewData(MovieDto movie) {
        if (movie == null) return null;
        
        Double avgRating = crudReviewEntity.getAverageRatingByMovieId(movie.id());
        long reviewCount = crudReviewEntity.countByMovieId(movie.id());
        
        return new MovieDto(
                movie.id(),
                movie.title(),
                movie.duration(),
                movie.genre(),
                movie.releaseDate(),
                movie.rating(),
                movie.available(),
                avgRating,
                reviewCount
        );
    }

    @Override
    public MovieDto save(MovieDto movieDto) {
        if(this.crudMovieEntity.findFirstByTitulo(movieDto.title()) != null){
            throw new MovieAlreadyException(movieDto.title());
        }

        MovieEntity movieEntity = movieMapper.toEntity(movieDto);
        movieEntity.setEstado("D");
        MovieDto savedMovie = this.movieMapper.toDto(crudMovieEntity.save(movieEntity));
        return enrichMovieWithReviewData(savedMovie);
    }

    @Override
    public MovieDto update(Long id, UpdateMovieDto updateMovieDto) {
        // 1. Verificar que la película existe
        MovieEntity movieEntity = crudMovieEntity.findById(id).orElse(null);
        if (movieEntity == null) {
            throw new MovieNotFoundException(id);
        }

        // 2. Si se está actualizando el título, verificar que no exista otra película con el mismo título
        if (updateMovieDto.title() != null && !updateMovieDto.title().equals(movieEntity.getTitulo())) {
            MovieEntity existingMovie = crudMovieEntity.findFirstByTitulo(updateMovieDto.title());
            if (existingMovie != null && !existingMovie.getId().equals(id)) {
                throw new MovieTitleAlreadyExistsException(updateMovieDto.title(), id);
            }
        }

        // 3. Actualizar la entidad
        this.movieMapper.updateEntityFromDto(updateMovieDto, movieEntity);

        MovieDto updatedMovie = this.movieMapper.toDto(this.crudMovieEntity.save(movieEntity));
        return enrichMovieWithReviewData(updatedMovie);
    }

    @Override
    public MovieDto update(UpdateMovieDto movieDto) {
        return null;
    }

    @Override
    public void delete(Long id) {
        // Verificar que la película existe antes de eliminar
        MovieEntity movieEntity = crudMovieEntity.findById(id).orElse(null);
        if (movieEntity == null) {
            throw new MovieNotFoundException(id);
        }
        
        this.crudMovieEntity.deleteById(id);
    }
}