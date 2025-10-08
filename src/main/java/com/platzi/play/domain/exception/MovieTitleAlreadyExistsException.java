package com.platzi.play.domain.exception;

public class MovieTitleAlreadyExistsException extends RuntimeException {
    public MovieTitleAlreadyExistsException(String movieTitle, Long currentMovieId) {
        super("Ya existe otra película con el título '" + movieTitle + "' (diferente a la que estás actualizando).");
    }
}