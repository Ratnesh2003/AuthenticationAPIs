package com.example.authentication.exceptions;

public class RoleDoesNotExistException extends RuntimeException{
    public RoleDoesNotExistException(String message) {
        super(message);
    }
}
