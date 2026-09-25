package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends MuvuException {
    public InvalidOperationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
