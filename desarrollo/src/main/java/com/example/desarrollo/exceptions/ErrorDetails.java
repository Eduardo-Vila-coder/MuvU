package com.example.desarrollo.exceptions;

import java.time.LocalDateTime;

public record ErrorDetails(
    int status,
    String error,
    String message,
    String path,
    LocalDateTime timestamp
){}