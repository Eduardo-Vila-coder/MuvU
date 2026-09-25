package com.example.desarrollo.service;

import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.PagoPublicidad;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.PagoPublicidadRepository;
import com.example.desarrollo.service.stripe.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoPublicidadService {

    private final PagoPublicidadRepository pagoPublicidadRepository;
    private final HabitacionRepository habitacionRepository;
    private final StripeService stripeService;
    private final ModelMapper modelMapper;

    @Transactional
    public PagoPublicidadResponseDTO registrarPago(PagoPublicidadRequestDTO requestDTO) {
        // 1. Buscamos la habitación a destacar
        Habitacion habitacion = habitacionRepository.findById(requestDTO.getHabitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + requestDTO.getHabitacionId()));

        // 2. Procesamos el cobro en Stripe
        try {
            stripeService.procesarCobro(habitacion.getId(), requestDTO.getMonto(), "usd");
        } catch (StripeException e) {
            throw new RuntimeException("Error al procesar el pago en Stripe: " + e.getMessage());
        }

        // 3. Activamos el destacado en la habitación
        habitacion.setEsDestacada(true);
        habitacionRepository.save(habitacion);

        // 4. Creamos y guardamos el registro de PagoPublicidad con sus fechas calculadas
        PagoPublicidad pago = modelMapper.map(requestDTO, PagoPublicidad.class);
        pago.setHabitacion(habitacion);

        // --- Calculamos las fechas ---
        LocalDate fechaInicio = LocalDate.now();
        pago.setFechaInicio(fechaInicio);
        pago.setFechaFin(calcularFechaFin(fechaInicio, requestDTO.getMonto()));
        // ---------------------------------------------

        pago = pagoPublicidadRepository.save(pago);

        return modelMapper.map(pago, PagoPublicidadResponseDTO.class);
    }

    public List<PagoPublicidadResponseDTO> listarTodos() {
        return pagoPublicidadRepository.findAll().stream()
                .map(pago -> {
                    PagoPublicidadResponseDTO dto = modelMapper.map(pago, PagoPublicidadResponseDTO.class);
                    if (pago.getHabitacion() != null) {
                        dto.setHabitacionId(pago.getHabitacion().getId());
                    }
                    dto.setMetodoDePago(pago.getMetodoPago()); // <-- Asignación explícita
                    return dto;
                })
                .toList();
    }

    //Tarifas fijas (30 soles = 30 días | 54 soles = 60 días)
    private LocalDate calcularFechaFin(LocalDate fechaInicio, Double monto) {
        if (Double.compare(monto, 30.0) == 0) { //bueno, resulta que el monto == 30.0, puede fallar xd
            return fechaInicio.plusDays(30);
        } else if (Double.compare(monto, 54.0) == 0) {
            return fechaInicio.plusDays(60);
        } else {
            throw new IllegalArgumentException("Monto no permitido. Solo son 2 opciones disponibles");
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
            }
        }
    }
}