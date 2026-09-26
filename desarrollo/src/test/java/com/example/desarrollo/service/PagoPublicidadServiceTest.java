package com.example.desarrollo.service;

import com.example.desarrollo.Events.NotificacionCorreoEvent;
import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.exceptions.ForbiddenException;
import com.example.desarrollo.exceptions.InvalidOperationException;
import com.example.desarrollo.exceptions.PaymentException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.PagoPublicidadRepository;
import com.example.desarrollo.service.stripe.StripeService;
import com.stripe.exception.ApiConnectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoPublicidadServiceTest {

    @Mock private PagoPublicidadRepository pagoPublicidadRepository;
    @Mock private HabitacionRepository habitacionRepository;
    @Mock private StripeService stripeService;
    @Spy private ModelMapper modelMapper = new ModelMapper();
    @Mock private UsuarioService usuarioService;
    @Mock private ApplicationEventPublisher publisher;

    @InjectMocks private PagoPublicidadService pagoPublicidadService;

    private Habitacion habitacion;

    @BeforeEach
    void setUp() {
        Arrendador arrendador = new Arrendador();
        arrendador.setId(2L);
        arrendador.setRol(Rol.ARRENDADOR);
        arrendador.setCorreo("rosa@muvu.com");

        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setDireccion("Av. Grau 100");
        habitacion.setArrendador(arrendador);
    }

    @Test
    void registrarPago_de30_destacaPor30Dias() throws Exception {
        prepararPago(2L);
        when(pagoPublicidadRepository.save(any(PagoPublicidad.class))).thenAnswer(inv -> inv.getArgument(0));

        PagoPublicidadResponseDTO respuesta = pagoPublicidadService.registrarPago(solicitud(30.0));

        assertEquals(LocalDate.now().plusDays(30), respuesta.getFechaFin());
        assertTrue(habitacion.getEsDestacada());
        verify(stripeService).procesarCobro(1L, 30.0, "usd");
        verify(publisher).publishEvent(any(NotificacionCorreoEvent.class));
    }

    @Test
    void registrarPago_de54_destacaPor60Dias() {
        prepararPago(2L);
        when(pagoPublicidadRepository.save(any(PagoPublicidad.class))).thenAnswer(inv -> inv.getArgument(0));

        PagoPublicidadResponseDTO respuesta = pagoPublicidadService.registrarPago(solicitud(54.0));

        assertEquals(LocalDate.now().plusDays(60), respuesta.getFechaFin());
    }

    @Test
    void registrarPago_montoNoPermitido_noCobra() throws Exception {
        prepararPago(2L);

        assertThrows(InvalidOperationException.class, () -> pagoPublicidadService.registrarPago(solicitud(40.0)));
        verify(stripeService, never()).procesarCobro(any(), any(), any());
    }

    @Test
    void registrarPago_deHabitacionAjena_lanzaForbidden() throws Exception {
        prepararPago(99L);

        assertThrows(ForbiddenException.class, () -> pagoPublicidadService.registrarPago(solicitud(30.0)));
        verify(stripeService, never()).procesarCobro(any(), any(), any());
    }

    @Test
    void registrarPago_stripeFalla_lanzaPaymentYNoDestaca() throws Exception {
        prepararPago(2L);
        when(stripeService.procesarCobro(1L, 30.0, "usd")).thenThrow(new ApiConnectionException("sin conexión"));

        assertThrows(PaymentException.class, () -> pagoPublicidadService.registrarPago(solicitud(30.0)));
        assertFalse(habitacion.getEsDestacada());
        verify(pagoPublicidadRepository, never()).save(any());
    }

    @Test
    void listarTodos_devuelveSoloLosPagosDelArrendador() {
        when(usuarioService.getIdUsuarioActual()).thenReturn(2L);
        when(pagoPublicidadRepository.findByHabitacionArrendadorId(2L)).thenReturn(List.of(pago(LocalDate.now())));

        assertEquals(1, pagoPublicidadService.listarTodos().size());
    }

    @Test
    void desactivarPublicidadesVencidas_quitaElDestacado() {
        habitacion.setEsDestacada(true);
        when(pagoPublicidadRepository.findByFechaFinBefore(any(LocalDate.class)))
                .thenReturn(List.of(pago(LocalDate.now().minusDays(1))));

        pagoPublicidadService.desactivarPublicidadesVencidas();

        assertFalse(habitacion.getEsDestacada());
        verify(habitacionRepository).save(habitacion);
    }

    private void prepararPago(Long miId) {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(usuarioService.getIdUsuarioActual()).thenReturn(miId);
    }

    private PagoPublicidadRequestDTO solicitud(Double monto) {
        return new PagoPublicidadRequestDTO(1L, monto, MetodoPago.TARJETA_CREDITO);
    }

    private PagoPublicidad pago(LocalDate fechaFin) {
        PagoPublicidad pago = new PagoPublicidad();
        pago.setId(4L);
        pago.setHabitacion(habitacion);
        pago.setMonto(30.0);
        pago.setMetodoPago(MetodoPago.TARJETA_CREDITO);
        pago.setFechaInicio(fechaFin.minusDays(30));
        pago.setFechaFin(fechaFin);
        return pago;
    }
}
