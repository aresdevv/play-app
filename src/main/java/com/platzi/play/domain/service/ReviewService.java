package com.platzi.play.domain.service;

import com.platzi.play.domain.dto.CreateReviewDto;
import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateReviewDto;
import com.platzi.play.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewDto createReview(CreateReviewDto createReviewDto, Long userId) {
        return reviewRepository.save(createReviewDto, userId);
    }

    @Transactional
    public ReviewDto updateReview(Long reviewId, UpdateReviewDto updateReviewDto, Long userId) {
        return reviewRepository.update(reviewId, updateReviewDto, userId);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        reviewRepository.delete(reviewId, userId);
    }

    public ReviewDto getReviewById(Long reviewId) {
        return reviewRepository.getById(reviewId);
    }

    public List<ReviewDto> getReviewsByMovieId(Long movieId) {
        return reviewRepository.getByMovieId(movieId);
    }

    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.getByUserId(userId);
    }

    public ReviewDto getUserReviewForMovie(Long userId, Long movieId) {
        return reviewRepository.getByUserIdAndMovieId(userId, movieId);
    }

    public boolean hasUserReviewedMovie(Long userId, Long movieId) {
        return reviewRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public Double getMovieAverageRating(Long movieId) {
        return reviewRepository.getAverageRatingByMovieId(movieId);
    }

    public long getMovieReviewCount(Long movieId) {
        return reviewRepository.countByMovieId(movieId);
    }
}

