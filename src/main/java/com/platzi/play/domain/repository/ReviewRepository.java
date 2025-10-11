package com.platzi.play.domain.repository;

import com.platzi.play.domain.dto.CreateReviewDto;
import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateReviewDto;

import java.util.List;

public interface ReviewRepository {
    
    ReviewDto save(CreateReviewDto createReviewDto, Long userId);
    
    ReviewDto update(Long reviewId, UpdateReviewDto updateReviewDto, Long userId);
    
    void delete(Long reviewId, Long userId);
    
    ReviewDto getById(Long reviewId);
    
    List<ReviewDto> getByMovieId(Long movieId);
    
    List<ReviewDto> getByUserId(Long userId);
    
    ReviewDto getByUserIdAndMovieId(Long userId, Long movieId);
    
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    Double getAverageRatingByMovieId(Long movieId);
    
    long countByMovieId(Long movieId);
}

