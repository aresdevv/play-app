package com.platzi.play.web.exception;

import com.platzi.play.domain.exception.MovieAlreadyException;
import com.platzi.play.domain.exception.MovieNotFoundException;
import com.platzi.play.domain.exception.MovieTitleAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MovieAlreadyException.class)
    public ResponseEntity<Error> handleMovieAlreadyExists(MovieAlreadyException exception) {
        Error error = new Error("movie-already-exists", exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Error> handleMovieNotFound(MovieNotFoundException exception) {
        Error error = new Error("movie-not-found", exception.getMessage());
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(MovieTitleAlreadyExistsException.class)
    public ResponseEntity<Error> handleMovieTitleAlreadyExists(MovieTitleAlreadyExistsException exception) {
        Error error = new Error("movie-title-already-exists", exception.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleException(MethodArgumentNotValidException ex){
        List<Error> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.add(new Error(error.getField(), error.getDefaultMessage()));
        });

        return ResponseEntity.badRequest().body(errors);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception ex){
        Error error = new Error("unknown-error", ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}
