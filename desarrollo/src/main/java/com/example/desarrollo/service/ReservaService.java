package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.Events.NotificacionCorreoEvent;
import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ForbiddenException;
import com.example.desarrollo.exceptions.InvalidOperationException;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioService usuarioService;
    private final ApplicationEventPublisher publisher;

    public ReservaResponseDTO findById(Long id) {
        Reserva reserva = buscarReserva(id);
        Long miId = usuarioService.getIdUsuarioActual();

        boolean soyElEstudiante = reserva.getEstudiante() != null
                && reserva.getEstudiante().getId().equals(miId);
        boolean soyElDueno = reserva.getHabitacion().getArrendador().getId().equals(miId);

        if (!soyElEstudiante && !soyElDueno) {
            throw new ForbiddenException("No tienes acceso a esta reserva");
        }
        return toDTO(reserva);
    }

    public Page<ReservaResponseDTO> findMisReservas(Pageable pageable) {
        Long miId = usuarioService.getIdUsuarioActual();
        Usuario yo = usuarioRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Page<Reserva> reservas = switch (yo.getRol()) {
            case ESTUDIANTE -> reservaRepository.findByEstudianteId(miId, pageable);
            case ARRENDADOR -> reservaRepository.findByHabitacionArrendadorId(miId, pageable);
            case ADMIN      -> reservaRepository.findAll(pageable);
        };

        return reservas.map(this::toDTO);
    }

    @Transactional
    public ReservaResponseDTO createReserva(ReservaRequestDTO dto) {
        Long miId = usuarioService.getIdUsuarioActual();
        Estudiante yo = estudianteRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro estudiante con id: " + miId));

        Habitacion habitacion = habitacionRepository.findById(dto.getHabitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro habitacion con id: " + dto.getHabitacionId()));

        validarFechas(habitacion.getId(), dto.getFecha_inicio(), dto.getFecha_fin());

        Reserva reserva = reservaRepository.save(new Reserva(dto.getFecha_inicio(), dto.getFecha_fin(), yo, habitacion));
        log.info("Reserva {} creada por el estudiante {} para la habitación {}", reserva.getId(), miId, habitacion.getId());

        notificar(yo.getCorreo(), "MuvU: solicitud de reserva enviada",
                "Hola " + yo.getNombre() + ", enviaste una solicitud para " + detalle(reserva)
                        + ". Te avisaremos cuando el arrendador la confirme.");
        notificar(habitacion.getArrendador().getCorreo(), "MuvU: nueva solicitud de reserva",
                yo.getNombre() + " quiere reservar " + detalle(reserva) + ". Ingresa a MuvU para confirmarla.");
        return toDTO(reserva);
    }

    // Solo el estudiante que hizo la reserva puede cancelarla
    @Transactional
    public ReservaResponseDTO cancelReserva(Long id) {
        Reserva reserva = buscarReserva(id);

        if (reserva.getEstudiante() == null
                || !reserva.getEstudiante().getId().equals(usuarioService.getIdUsuarioActual())) {
            throw new ForbiddenException("Solo el estudiante que hizo la reserva puede cancelarla");
        }
        if (reserva.getEstado() == Estado.CANCELADO) {
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya cancelada");
        } else if (reserva.getEstado() == Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya confirmada");
        }

        reserva.setEstado(Estado.CANCELADO);
        log.info("Reserva {} cancelada", id);
        notificar(reserva.getHabitacion().getArrendador().getCorreo(), "MuvU: reserva cancelada",
                reserva.getEstudiante().getNombre() + " canceló su solicitud para " + detalle(reserva) + ".");
        return toDTO(reservaRepository.save(reserva));
    }

    // Solo el arrendador dueño de la habitación puede confirmar
    @Transactional
    public ReservaResponseDTO confirmReserva(Long id) {
        Reserva reserva = buscarReserva(id);

        if (!reserva.getHabitacion().getArrendador().getId().equals(usuarioService.getIdUsuarioActual())) {
            throw new ForbiddenException("Solo el dueño de la habitación puede confirmar la reserva");
        }
        if (reserva.getEstado() == Estado.CANCELADO) {
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya cancelada");
        } else if (reserva.getEstado() == Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya confirmada");
        }

        reserva.setEstado(Estado.CONFIRMADO);
        log.info("Reserva {} confirmada", id);
        notificar(reserva.getEstudiante().getCorreo(), "MuvU: tu reserva fue confirmada",
                "Hola " + reserva.getEstudiante().getNombre() + ", el arrendador confirmó tu reserva para "
                        + detalle(reserva) + ".");
        return toDTO(reservaRepository.save(reserva));
    }

    // ---------- helpers ----------

    private void notificar(String correo, String asunto, String mensaje) {
        publisher.publishEvent(new NotificacionCorreoEvent(this, Mail.para(correo, asunto, mensaje)));
    }

    private String detalle(Reserva r) {
        return "la habitación en " + r.getHabitacion().getDireccion()
                + " del " + r.getFecha_inicio() + " al " + r.getFecha_fin();
    }

    private void validarFechas(Long habitacionId, LocalDate inicio, LocalDate fin) {
        if (!fin.isAfter(inicio)) {
            throw new InvalidOperationException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        boolean ocupada = reservaRepository
                .findByHabitacionIdAndEstadoIn(habitacionId, List.of(Estado.PENDIENTE, Estado.CONFIRMADO))
                .stream()
                .anyMatch(r -> r.seCruzaCon(inicio, fin));
        if (ocupada) {
            log.warn("Habitación {} ya reservada entre {} y {}", habitacionId, inicio, fin);
            throw new ConflictException("La habitación ya está reservada en esas fechas");
        }
    }

    private Reserva buscarReserva(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe reserva con el ID: " + id));
    }

    private ReservaResponseDTO toDTO(Reserva r) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(r.getId());
        dto.setEstado(r.getEstado());
        dto.setFecha_inicio(r.getFecha_inicio());
        dto.setFecha_fin(r.getFecha_fin());
        dto.setHabitacionId(r.getHabitacion() != null ? r.getHabitacion().getId() : null);
        dto.setEstudianteId(r.getEstudiante() != null ? r.getEstudiante().getId() : null);
        return dto;
    }
}
