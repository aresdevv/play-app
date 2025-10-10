package com.platzi.play.persistence.crud;

import com.platzi.play.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CrudReviewEntity extends JpaRepository<ReviewEntity, Long> {

    // Obtener todas las reviews de una película
    List<ReviewEntity> findByMovieId(Long movieId);

    // Obtener todas las reviews de un usuario
    List<ReviewEntity> findByUserId(Long userId);

    // Verificar si un usuario ya hizo review de una película
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    // Obtener la review de un usuario para una película específica
    Optional<ReviewEntity> findByUserIdAndMovieId(Long userId, Long movieId);

    // Calcular el rating promedio de una película
    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.movie.id = :movieId")
    Double getAverageRatingByMovieId(@Param("movieId") Long movieId);

    // Contar reviews de una película
    long countByMovieId(Long movieId);
}

