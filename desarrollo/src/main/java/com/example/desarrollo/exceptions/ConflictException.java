package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends MuvuException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
