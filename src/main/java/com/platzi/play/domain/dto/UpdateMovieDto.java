package com.platzi.play.domain.dto;

import com.platzi.play.domain.Genre;

import java.time.LocalDate;

public record UpdateMovieDto(
        String title,
        LocalDate releaseDate,
        Double rating
) {
}
