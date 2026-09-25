package com.example.desarrollo.exceptions;

public class ReservaInvalidStateException extends ConflictException {
    public ReservaInvalidStateException(String message) {
        super(message);
    }
}
