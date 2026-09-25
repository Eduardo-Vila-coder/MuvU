package com.example.desarrollo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

// Formato único de todas las respuestas de error de la API
@JsonInclude(JsonInclude.Include.NON_NULL)   // "errores" solo aparece en errores de validación
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> errores
) {}
