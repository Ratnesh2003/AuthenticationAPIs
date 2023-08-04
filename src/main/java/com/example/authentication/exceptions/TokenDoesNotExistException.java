package com.example.authentication.exceptions;

public class TokenDoesNotExistException extends RuntimeException {
    public TokenDoesNotExistException(String message) {
        super(message);
    }
}
