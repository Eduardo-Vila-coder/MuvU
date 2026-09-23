package com.example.desarrollo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

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
}
