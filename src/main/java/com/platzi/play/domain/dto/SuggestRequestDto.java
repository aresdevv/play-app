package com.platzi.play.domain.dto;

import com.platzi.play.domain.Genre;

import java.time.LocalDate;

public record SuggestRequestDto(
     String userPreferences
) {
}
