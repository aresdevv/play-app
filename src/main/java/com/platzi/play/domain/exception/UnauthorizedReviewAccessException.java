package com.platzi.play.domain.exception;

public class UnauthorizedReviewAccessException extends RuntimeException {
    public UnauthorizedReviewAccessException() {
        super("No tienes permisos para modificar esta review");
    }

    public UnauthorizedReviewAccessException(String message) {
        super(message);
    }
}

