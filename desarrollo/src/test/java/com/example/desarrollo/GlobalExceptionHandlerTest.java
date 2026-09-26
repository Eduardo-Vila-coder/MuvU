package com.example.desarrollo;

import com.example.desarrollo.dto.ErrorResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("GET", "/habitacion/1");

    @Test
    void excepcionPropia_respondeConSuStatusYMensaje() {
        ResponseEntity<ErrorResponseDTO> r = handler.handleMuvu(new ResourceNotFoundException("No existe"), request);

        assertEquals(404, r.getStatusCode().value());
        assertEquals("No existe", r.getBody().message());
        assertEquals("/habitacion/1", r.getBody().path());
        assertEquals(409, handler.handleMuvu(new ConflictException("x"), request).getStatusCode().value());
    }

    @Test
    void validacionDeDTO_devuelveElErrorDeCadaCampo() throws Exception {
        BeanPropertyBindingResult errores = new BeanPropertyBindingResult(new Object(), "dto");
        errores.addError(new FieldError("dto", "correo", "El correo es obligatorio"));

        ResponseEntity<ErrorResponseDTO> r = handler.handleValidacion(
                new MethodArgumentNotValidException(parametro(), errores), request);

        assertEquals(400, r.getStatusCode().value());
        assertEquals("El correo es obligatorio", r.getBody().errores().get("correo"));
    }

    @Test
    void erroresDePeticion_responden400() throws Exception {
        assertEquals(400, handler.handleBodyIlegible(
                new HttpMessageNotReadableException("x", new MockHttpInputMessage(new byte[0])), request).getStatusCode().value());
        assertEquals(400, handler.handleTipoIncorrecto(
                new MethodArgumentTypeMismatchException("abc", Long.class, "id", parametro(), null), request).getStatusCode().value());
        assertEquals(400, handler.handleParametroFaltante(
                new MissingServletRequestParameterException("universidadId", "Long"), request).getStatusCode().value());
    }

    @Test
    void seguridad_responde401y403() {
        assertEquals(401, handler.handleBadCredentials(new BadCredentialsException("x"), request).getStatusCode().value());
        assertEquals(401, handler.handleNoAutenticado(new InsufficientAuthenticationException("x"), request).getStatusCode().value());
        assertEquals(403, handler.handleAccessDenied(new AccessDeniedException("x"), request).getStatusCode().value());
    }

    @Test
    void metodoNoPermitido_responde405() {
        ResponseEntity<ErrorResponseDTO> r = handler.handleMethodNotSupported(
                new HttpRequestMethodNotSupportedException("PATCH", List.of("GET")), request);

        assertEquals(405, r.getStatusCode().value());
    }

    @Test
    void integridadDeDatos_responde409() {
        assertEquals(409, handler.handleDataIntegrity(new DataIntegrityViolationException("fk"), request).getStatusCode().value());
    }

    @Test
    void errorNoPrevisto_responde500SinExponerDetalles() {
        ResponseEntity<ErrorResponseDTO> r = handler.handleGenerico(new RuntimeException("NullPointer interno"), request);

        assertEquals(500, r.getStatusCode().value());
        assertEquals("Error interno del servidor", r.getBody().message());
    }

    private MethodParameter parametro() throws NoSuchMethodException {
        return new MethodParameter(getClass().getDeclaredMethod("metodoDePrueba", String.class), 0);
    }

    @SuppressWarnings("unused")
    void metodoDePrueba(String valor) {
    }
}
