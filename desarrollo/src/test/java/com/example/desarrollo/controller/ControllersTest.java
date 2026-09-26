package com.example.desarrollo.controller;

import com.example.desarrollo.dto.*;
import com.example.desarrollo.dto.Logueo.LoginRequestDTO;
import com.example.desarrollo.dto.Logueo.TokenResponseDTO;
import com.example.desarrollo.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllersTest {

    @Mock private AuthService authService;
    @Mock private ArrendadorService arrendadorService;
    @Mock private CalificacionService calificacionService;
    @Mock private EstudianteService estudianteService;
    @Mock private HabitacionService habitacionService;
    @Mock private ImagenService imagenService;
    @Mock private PagoPublicidadService pagoPublicidadService;
    @Mock private ReservaService reservaService;
    @Mock private UniversidadService universidadService;

    @AfterEach
    void limpiar() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void auth_registroResponde201YLogin200() {
        AuthController controller = new AuthController(authService);
        TokenResponseDTO token = new TokenResponseDTO("jwt", "ESTUDIANTE", 1L);
        when(authService.registerEstudiante(any())).thenReturn(token);
        when(authService.registerArrendador(any())).thenReturn(token);
        when(authService.login(any())).thenReturn(token);

        assertEquals(201, controller.registerEstudiante(new EstudianteRequestDTO()).getStatusCode().value());
        assertEquals(201, controller.registerArrendador(new ArrendadorRequestDTO()).getStatusCode().value());
        ResponseEntity<TokenResponseDTO> login = controller.login(new LoginRequestDTO());
        assertEquals(200, login.getStatusCode().value());
        assertSame(token, login.getBody());
    }

    @Test
    void arrendador_delegaEnElServicio() {
        ArrendadorController controller = new ArrendadorController(arrendadorService);

        assertEquals(200, controller.getArrendadorById(2L).getStatusCode().value());
        assertEquals(200, controller.updateArrendador(2L, new ArrendadorUpdateRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.verificarArrendador(2L).getStatusCode().value());
        assertTrue(controller.deleteArrendador(2L).getStatusCode().is2xxSuccessful());
        verify(arrendadorService).verificar(2L);
        verify(arrendadorService).deleteById(2L);
    }

    @Test
    void calificacion_creaCon201YEliminaCon204() {
        CalificacionController controller = new CalificacionController(calificacionService);

        assertEquals(201, controller.createCalificacion(new CalificacionRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.getById(5L).getStatusCode().value());
        assertEquals(200, controller.getByHabitacion(1L).getStatusCode().value());
        assertEquals(200, controller.getByEstudiante(3L).getStatusCode().value());
        assertEquals(204, controller.deleteCalificacion(5L).getStatusCode().value());
        verify(calificacionService).deleteById(5L);
    }

    @Test
    void estudiante_delegaEnElServicio() {
        EstudianteController controller = new EstudianteController(estudianteService);

        assertEquals(200, controller.getById(3L).getStatusCode().value());
        assertEquals(200, controller.getAll(PageRequest.of(0, 20)).getStatusCode().value());
        assertEquals(200, controller.updateEstudiante(3L, new EstudianteUpdateRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.getPerfil(3L).getStatusCode().value());
        assertEquals(204, controller.delete(3L).getStatusCode().value());
        verify(estudianteService).delete(3L);
    }

    @Test
    void habitacion_creaCon201YLocation() {
        simularPeticion("/habitacion");
        HabitacionController controller = new HabitacionController(habitacionService);
        HabitacionResponseDTO creada = new HabitacionResponseDTO();
        creada.setId(1L);
        when(habitacionService.save(any())).thenReturn(creada);

        ResponseEntity<HabitacionResponseDTO> respuesta = controller.createHabitacion(new HabitacionRequestDTO());

        assertEquals(201, respuesta.getStatusCode().value());
        assertTrue(respuesta.getHeaders().getLocation().toString().endsWith("/habitacion/1"));
        assertEquals(200, controller.getHabitacionesCercanas(1L, 3.0, 0, 10).getStatusCode().value());
        assertEquals(200, controller.getHabitacionById(1L).getStatusCode().value());
        assertEquals(200, controller.getAllHabitaciones(0, 10).getStatusCode().value());
        assertEquals(200, controller.updateHabitacion(1L, new HabitacionRequestDTO()).getStatusCode().value());
        assertEquals(204, controller.deleteHabitacion(1L).getStatusCode().value());
        verify(habitacionService).findCercanas(1L, 3.0, PageRequest.of(0, 10));
    }

    @Test
    void imagen_agregaCon201YEliminaCon204() {
        ImagenController controller = new ImagenController(imagenService);

        assertEquals(201, controller.agregarImagen(1L, new ImagenRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.obtenerImagenes(1L).getStatusCode().value());
        assertEquals(204, controller.eliminarImagen(7L).getStatusCode().value());
        verify(imagenService).eliminarImagen(7L);
    }

    @Test
    void pagoPublicidad_registraCon201() {
        PagoPublicidadController controller = new PagoPublicidadController(pagoPublicidadService);

        assertEquals(201, controller.registrarPago(new PagoPublicidadRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.listarTodos().getStatusCode().value());
    }

    @Test
    void reserva_creaCon201YLocation() {
        ReservaController controller = new ReservaController(reservaService);
        ReservaResponseDTO creada = new ReservaResponseDTO();
        creada.setId(5L);
        when(reservaService.createReserva(any())).thenReturn(creada);

        ResponseEntity<ReservaResponseDTO> respuesta = controller.createReserva(new ReservaRequestDTO());

        assertEquals(201, respuesta.getStatusCode().value());
        assertEquals("/reservas/5", respuesta.getHeaders().getLocation().toString());
        assertEquals(200, controller.getMisReservas().getStatusCode().value());
        assertEquals(200, controller.getReservaById(5L).getStatusCode().value());
        assertEquals(200, controller.updateReserva(5L).getStatusCode().value());
        assertEquals(200, controller.confirmReserva(5L).getStatusCode().value());
        verify(reservaService).cancelReserva(5L);
        verify(reservaService).confirmReserva(5L);
    }

    @Test
    void universidad_creaCon201YEliminaCon204() {
        simularPeticion("/universidad");
        UniversidadController controller = new UniversidadController(universidadService);
        UniversidadResponseDTO creada = new UniversidadResponseDTO();
        creada.setId(1L);
        when(universidadService.createUniversidad(any())).thenReturn(creada);

        assertEquals(201, controller.createUniversidad(new UniversidadRequestDTO()).getStatusCode().value());
        assertEquals(200, controller.getAllUnis().getStatusCode().value());
        assertEquals(200, controller.getUniById(1L).getStatusCode().value());
        assertEquals(204, controller.deleteUni(1L).getStatusCode().value());
        verify(universidadService).deleteById(1L);
    }

    private void simularPeticion(String uri) {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest("POST", uri)));
    }
}
