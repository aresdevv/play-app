package com.platzi.play.persistence.crud;

import com.platzi.play.persistence.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CrudMovieEntity extends JpaRepository<MovieEntity, Long> {

    MovieEntity findFirstByTitle(String title);
    
    Optional<MovieEntity> findByTmdbId(Long tmdbId);

}
