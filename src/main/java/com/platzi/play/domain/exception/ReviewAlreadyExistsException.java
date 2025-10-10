package com.platzi.play.domain.exception;

public class ReviewAlreadyExistsException extends RuntimeException {
    public ReviewAlreadyExistsException() {
        super("Ya existe una review para esta película");
    }

    public ReviewAlreadyExistsException(Long userId, Long movieId) {
        super("El usuario " + userId + " ya hizo una review de la película " + movieId);
    }

    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}

