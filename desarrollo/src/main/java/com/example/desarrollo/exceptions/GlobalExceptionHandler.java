package com.example.desarrollo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ReservaInvalidStateException.class})
    public ProblemDetail handlerReservaInvalidState(ReservaInvalidStateException problem) {
        ProblemDetail problemDetail=ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                problem.getMessage()
        );
        problemDetail.setTitle("Conflicto con el estado actual de reserva");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
    @ExceptionHandler({ConflictException.class})// lo pondre como algo generico por si se quiere especificar no hay problema
    public ProblemDetail handlerConflict(ConflictException conflict){
        ProblemDetail problem= ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                conflict.getMessage()
        );
        problem.setTitle("Conflicto encontrado");
        problem.setProperty("timestamp",Instant.now());
        return problem;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ProblemDetail handlerResourceNotFound(ResourceNotFoundException resourceNotFoundException){
        ProblemDetail problemDetail= ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                resourceNotFoundException.getMessage()
        );
        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setProperty("timestamp",Instant.now());
        return problemDetail;
    }
    public ResponseEntity<ErrorDetails> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        String message = String.format("El método HTTP '%s' no está permitido para esta ruta. Métodos soportados: %s", ex.getMethod(), ex.getSupportedHttpMethods());
        ErrorDetails error = new ErrorDetails(
                HttpStatus.METHOD_NOT_ALLOWED.value(),        // 405
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), // "Method Not Allowed"
                message,
                request.getRequestURI(),                        // URI intentada
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }
}
