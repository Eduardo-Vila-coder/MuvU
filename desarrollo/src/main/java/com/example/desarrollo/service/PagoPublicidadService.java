package com.example.desarrollo.service;

import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.PagoPublicidad;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.PagoPublicidadRepository;
import com.example.desarrollo.service.stripe.StripeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
        // 1. Buscar la habitación asociada
        Habitacion habitacion = habitacionRepository.findById(requestDTO.getHabitacionId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + requestDTO.getHabitacionId()));

        // 2. Procesar el pago con Stripe
        try {
            stripeService.procesarCobro(requestDTO.getMonto(), "pen");
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el pago con la pasarela Stripe: " + e.getMessage());
        }

        // 3. Instanciar entidad y asignar fechas según la regla de negocio
        PagoPublicidad pago = new PagoPublicidad();
        pago.setHabitacion(habitacion);
        pago.setMonto(requestDTO.getMonto());
        pago.setMetodoPago(requestDTO.getMetodoDePago());

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = calcularFechaFin(fechaInicio, requestDTO.getMonto());

        pago.setFechaInicio(fechaInicio);
        pago.setFechaFin(fechaFin);

        // 4. Guardar en Base de Datos
        PagoPublicidad pagoGuardado = pagoPublicidadRepository.save(pago);

        // 5. Mapear y responder con ResponseDTO
        PagoPublicidadResponseDTO responseDTO = modelMapper.map(pagoGuardado, PagoPublicidadResponseDTO.class);
        responseDTO.setHabitacionId(habitacion.getId());
        responseDTO.setMetodoDePago(pagoGuardado.getMetodoPago()); // <-- Asignación explícita

        return responseDTO;
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
}