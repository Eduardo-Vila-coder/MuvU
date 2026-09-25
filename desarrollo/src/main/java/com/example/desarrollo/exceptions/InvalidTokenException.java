package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends MuvuException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
