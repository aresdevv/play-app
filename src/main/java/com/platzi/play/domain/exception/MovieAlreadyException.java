package com.platzi.play.domain.exception;

public class MovieAlreadyException extends RuntimeException{
    public MovieAlreadyException(String movieTitle) {
        super("La pelicula " + movieTitle + " ya existe.");
    }
}
