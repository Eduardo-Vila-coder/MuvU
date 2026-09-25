package com.example.desarrollo;

import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ErrorDetails;
import com.example.desarrollo.exceptions.MuvuException;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Resto de excepciones propias (Forbidden, InvalidOperation, ExternalService, Payment): usan su propio status
    @ExceptionHandler(MuvuException.class)
    public ProblemDetail handlerMuvu(MuvuException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        p.setTitle(ex.getStatus().getReasonPhrase());
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
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

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handlerBadCredentials(BadCredentialsException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
        p.setTitle("Credenciales inválidas");
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handlerAccessDenied(AccessDeniedException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        p.setTitle("Acceso denegado");
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handlerIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        p.setTitle("Solicitud inválida");
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handlerDataIntegrity(DataIntegrityViolationException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "No se puede completar la operación porque el registro está relacionado con otros datos");
        p.setTitle("Conflicto de integridad de datos");
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    // Fallos de servicios externos (Stripe, Google Maps, correo)
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handlerIllegalState(IllegalStateException ex) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.getMessage());
        p.setTitle("Error en un servicio externo");
        p.setProperty("timestamp", Instant.now());
        return p;
    }

    // Errores de @Valid en los DTOs: devuelve el mensaje de cada campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handlerValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage(), (a, b) -> a));

        ProblemDetail p = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos inválidos");
        p.setTitle("Error de validación");
        p.setProperty("errores", errores);
        p.setProperty("timestamp", Instant.now());
        return p;
    }
}
