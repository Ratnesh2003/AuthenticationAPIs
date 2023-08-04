package com.example.authentication.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String message) {
        super(message);
    }
}
