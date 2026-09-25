package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends MuvuException {
    public ExternalServiceException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY, cause);
    }
}
