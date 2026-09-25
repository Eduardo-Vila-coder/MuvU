package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends MuvuException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
