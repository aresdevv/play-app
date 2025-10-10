package com.platzi.play.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("El usuario con ID " + userId + " no existe.");
    }
    
    public UserNotFoundException(String usernameOrEmail) {
        super("El usuario '" + usernameOrEmail + "' no existe.");
    }
}
