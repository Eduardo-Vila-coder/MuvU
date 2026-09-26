package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.Events.NotificacionCorreoEvent;
import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.exceptions.ForbiddenException;
import com.example.desarrollo.exceptions.InvalidOperationException;
import com.example.desarrollo.exceptions.PaymentException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Mail;
import com.example.desarrollo.model.PagoPublicidad;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.PagoPublicidadRepository;
import com.example.desarrollo.service.stripe.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoPublicidadService {

    private final PagoPublicidadRepository pagoPublicidadRepository;
    private final HabitacionRepository habitacionRepository;
    private final StripeService stripeService;
    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public PagoPublicidadResponseDTO registrarPago(PagoPublicidadRequestDTO requestDTO) {
        // 1. Buscamos la habitación a destacar
        Habitacion habitacion = habitacionRepository.findById(requestDTO.getHabitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + requestDTO.getHabitacionId()));

        if (!habitacion.getArrendador().getId().equals(usuarioService.getIdUsuarioActual())) {
            throw new ForbiddenException("Solo el dueño puede pagar publicidad para esta habitación");
        }

        // 2. Validamos el monto ANTES de cobrar
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = calcularFechaFin(fechaInicio, requestDTO.getMonto());

        // 3. Procesamos el cobro en Stripe
        try {
            stripeService.procesarCobro(habitacion.getId(), requestDTO.getMonto(), "usd");
        } catch (StripeException e) {
            log.error("Stripe rechazó el cobro de la habitación {}: {}", habitacion.getId(), e.getMessage());
            throw new PaymentException("Error al procesar el pago en Stripe: " + e.getMessage(), e);
        }

        // 4. Activamos el destacado en la habitación
        habitacion.setEsDestacada(true);
        habitacionRepository.save(habitacion);

        // 5. Creamos y guardamos el registro de PagoPublicidad con sus fechas calculadas
        // Se arma a mano: ModelMapper copiaba "habitacionId" también al "id" del pago
        PagoPublicidad pago = new PagoPublicidad();
        pago.setHabitacion(habitacion);
        pago.setMonto(requestDTO.getMonto());
        pago.setMetodoPago(requestDTO.getMetodoPago());
        pago.setFechaInicio(fechaInicio);
        pago.setFechaFin(fechaFin);

        pago = pagoPublicidadRepository.save(pago);
        log.info("Pago {} registrado: habitación {} destacada hasta {}", pago.getId(), habitacion.getId(), fechaFin);

        Arrendador arrendador = habitacion.getArrendador();
        publisher.publishEvent(new NotificacionCorreoEvent(this, Mail.para(arrendador.getCorreo(),
                "MuvU: pago de publicidad confirmado",
                "Hola " + arrendador.getNombre() + ", recibimos tu pago de " + requestDTO.getMonto()
                        + ". Tu habitación en " + habitacion.getDireccion() + " estará destacada hasta el " + fechaFin + ".")));

        return modelMapper.map(pago, PagoPublicidadResponseDTO.class);
    }

    // Cada arrendador solo ve los pagos de sus propias habitaciones
    public List<PagoPublicidadResponseDTO> listarTodos() {
        return pagoPublicidadRepository.findByHabitacionArrendadorId(usuarioService.getIdUsuarioActual()).stream()
                .map(pago -> modelMapper.map(pago, PagoPublicidadResponseDTO.class))
                .toList();
    }

    // Tarifas fijas: 30 soles = 30 días | 54 soles = 60 días
    private LocalDate calcularFechaFin(LocalDate fechaInicio, Double monto) {
        if (Double.compare(monto, 30.0) == 0) {
            return fechaInicio.plusDays(30);
        } else if (Double.compare(monto, 54.0) == 0) {
            return fechaInicio.plusDays(60);
        } else {
            log.warn("Monto de publicidad no permitido: {}", monto);
            throw new InvalidOperationException("Monto no permitido. Solo son 2 opciones disponibles");
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void desactivarPublicidadesVencidas() {
        List<PagoPublicidad> pagosVencidos = pagoPublicidadRepository.findByFechaFinBefore(LocalDate.now());

        for (PagoPublicidad pago : pagosVencidos) {
            Habitacion habitacion = pago.getHabitacion();
            if (Boolean.TRUE.equals(habitacion.getEsDestacada())) {
                habitacion.setEsDestacada(false);
                habitacionRepository.save(habitacion);
                log.info("Publicidad vencida: la habitación {} dejó de estar destacada", habitacion.getId());
            }
        }
    }
}