package com.platzi.play.persistence;

import com.platzi.play.domain.dto.CreateReviewDto;
import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateReviewDto;
import com.platzi.play.domain.exception.MovieNotFoundException;
import com.platzi.play.domain.exception.ReviewAlreadyExistsException;
import com.platzi.play.domain.exception.ReviewNotFoundException;
import com.platzi.play.domain.exception.UnauthorizedReviewAccessException;
import com.platzi.play.domain.exception.UserNotFoundException;
import com.platzi.play.domain.repository.ReviewRepository;
import com.platzi.play.persistence.crud.CrudMovieEntity;
import com.platzi.play.persistence.crud.CrudReviewEntity;
import com.platzi.play.persistence.crud.CrudUserEntity;
import com.platzi.play.persistence.entity.MovieEntity;
import com.platzi.play.persistence.entity.ReviewEntity;
import com.platzi.play.persistence.entity.UserEntity;
import com.platzi.play.persistence.mapper.ReviewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewEntityRepository implements ReviewRepository {

    private final CrudReviewEntity crudReviewEntity;
    private final CrudUserEntity crudUserEntity;
    private final CrudMovieEntity crudMovieEntity;
    private final ReviewMapper reviewMapper;

    public ReviewEntityRepository(CrudReviewEntity crudReviewEntity, 
                                  CrudUserEntity crudUserEntity,
                                  CrudMovieEntity crudMovieEntity,
                                  ReviewMapper reviewMapper) {
        this.crudReviewEntity = crudReviewEntity;
        this.crudUserEntity = crudUserEntity;
        this.crudMovieEntity = crudMovieEntity;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewDto save(CreateReviewDto createReviewDto, Long userId) {
        // Verificar que el usuario existe
        UserEntity user = crudUserEntity.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Verificar que la película existe
        MovieEntity movie = crudMovieEntity.findById(createReviewDto.movieId())
                .orElseThrow(() -> new MovieNotFoundException(createReviewDto.movieId()));

        // Verificar que el usuario no haya hecho review de esta película
        if (crudReviewEntity.existsByUserIdAndMovieId(userId, createReviewDto.movieId())) {
            throw new ReviewAlreadyExistsException(userId, createReviewDto.movieId());
        }

        // Crear la review
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setUser(user);
        reviewEntity.setMovie(movie);
        reviewEntity.setRating(createReviewDto.rating());
        reviewEntity.setComment(createReviewDto.comment());

        ReviewEntity savedReview = crudReviewEntity.save(reviewEntity);
        return reviewMapper.toReviewDto(savedReview);
    }

    @Override
    public ReviewDto update(Long reviewId, UpdateReviewDto updateReviewDto, Long userId) {
        ReviewEntity reviewEntity = crudReviewEntity.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        // Verificar que el usuario sea el dueño de la review
        if (!reviewEntity.getUser().getId().equals(userId)) {
            throw new UnauthorizedReviewAccessException();
        }

        // Actualizar solo los campos que no son null
        if (updateReviewDto.rating() != null) {
            reviewEntity.setRating(updateReviewDto.rating());
        }
        if (updateReviewDto.comment() != null) {
            reviewEntity.setComment(updateReviewDto.comment());
        }

        ReviewEntity updatedReview = crudReviewEntity.save(reviewEntity);
        return reviewMapper.toReviewDto(updatedReview);
    }

    @Override
    public void delete(Long reviewId, Long userId) {
        ReviewEntity reviewEntity = crudReviewEntity.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));

        // Verificar que el usuario sea el dueño de la review
        if (!reviewEntity.getUser().getId().equals(userId)) {
            throw new UnauthorizedReviewAccessException();
        }

        crudReviewEntity.delete(reviewEntity);
    }

    @Override
    public ReviewDto getById(Long reviewId) {
        ReviewEntity reviewEntity = crudReviewEntity.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        return reviewMapper.toReviewDto(reviewEntity);
    }

    @Override
    public List<ReviewDto> getByMovieId(Long movieId) {
        List<ReviewEntity> reviews = crudReviewEntity.findByMovieId(movieId);
        return reviewMapper.toReviewDtos(reviews);
    }

    @Override
    public List<ReviewDto> getByUserId(Long userId) {
        List<ReviewEntity> reviews = crudReviewEntity.findByUserId(userId);
        return reviewMapper.toReviewDtos(reviews);
    }

    @Override
    public ReviewDto getByUserIdAndMovieId(Long userId, Long movieId) {
        ReviewEntity reviewEntity = crudReviewEntity.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new ReviewNotFoundException(
                        "No se encontró review del usuario " + userId + " para la película " + movieId));
        return reviewMapper.toReviewDto(reviewEntity);
    }

    @Override
    public boolean existsByUserIdAndMovieId(Long userId, Long movieId) {
        return crudReviewEntity.existsByUserIdAndMovieId(userId, movieId);
    }

    @Override
    public Double getAverageRatingByMovieId(Long movieId) {
        return crudReviewEntity.getAverageRatingByMovieId(movieId);
    }

    @Override
    public long countByMovieId(Long movieId) {
        return crudReviewEntity.countByMovieId(movieId);
    }
}

