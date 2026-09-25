package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends MuvuException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
