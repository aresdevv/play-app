package com.platzi.play.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateReviewDto(
        @Min(value = 1, message = "La calificación mínima es 1")
        @Max(value = 5, message = "La calificación máxima es 5")
        Integer rating,

        @Size(max = 1000, message = "El comentario no puede exceder 1000 caracteres")
        String comment
) {
}

