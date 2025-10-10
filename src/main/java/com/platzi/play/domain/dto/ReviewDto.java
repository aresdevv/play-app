package com.platzi.play.domain.dto;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Long userId,
        String username, // Para mostrar quién hizo la review
        Long movieId,
        String movieTitle, // Para mostrar en qué película
        Integer rating, // 1-5 estrellas
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

