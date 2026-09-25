package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

// Base de todas las excepciones propias: cada una define el status HTTP con el que responde
public abstract class MuvuException extends RuntimeException {

    private final HttpStatus status;

    protected MuvuException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected MuvuException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
