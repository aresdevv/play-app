package com.platzi.play.domain.dto;

import com.platzi.play.domain.Genre;

import java.time.LocalDate;

public record MovieDto(
        Long id,
        String title,
        Integer duration,
        Genre genre,
        LocalDate releaseDate,
        Double rating,
        boolean available
) {
}
