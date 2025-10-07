package com.platzi.play.web.exception;

import com.platzi.play.domain.exception.MovieAlreadyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MovieAlreadyException.class)
    public ResponseEntity<Error> handleException(MovieAlreadyException exception) {
        Error error = new Error("movie-Already-exists", exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
