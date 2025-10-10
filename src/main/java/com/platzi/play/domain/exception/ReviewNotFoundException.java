package com.platzi.play.domain.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException() {
        super("Review no encontrada");
    }

    public ReviewNotFoundException(Long id) {
        super("Review con ID " + id + " no encontrada");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}

