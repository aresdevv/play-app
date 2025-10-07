package com.platzi.play.domain.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Long movieId) {
        super("La película con ID " + movieId + " no existe.");
    }
    
    public MovieNotFoundException(String movieTitle) {
        super("La película '" + movieTitle + "' no existe.");
    }
}
