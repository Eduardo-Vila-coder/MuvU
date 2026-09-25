package com.example.desarrollo.service;

import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioService usuarioService;

    public ReservaResponseDTO findById(Long id) {
        Reserva reserva = buscarReserva(id);
        Long miId = usuarioService.getIdUsuarioActual();

        boolean soyElEstudiante = reserva.getEstudiante() != null
                && reserva.getEstudiante().getId().equals(miId);
        boolean soyElDueno = reserva.getHabitacion().getArrendador().getId().equals(miId);

        if (!soyElEstudiante && !soyElDueno) {
            throw new AccessDeniedException("No tienes acceso a esta reserva");
        }
        return toDTO(reserva);
    }

    public List<ReservaResponseDTO> findMisReservas() {
        Long miId = usuarioService.getIdUsuarioActual();
        Usuario yo = usuarioRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<Reserva> reservas = switch (yo.getRol()) {
            case ESTUDIANTE -> reservaRepository.findByEstudianteId(miId);
            case ARRENDADOR -> reservaRepository.findByHabitacionArrendadorId(miId);
            case ADMIN      -> reservaRepository.findAll();
        };

        return reservas.stream().map(this::toDTO).toList();
    }

    @Transactional
    public ReservaResponseDTO createReserva(ReservaRequestDTO dto) {
        Long miId = usuarioService.getIdUsuarioActual();
        Estudiante yo = estudianteRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro estudiante con id: " + miId));

        Habitacion habitacion = habitacionRepository.findById(dto.getHabitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro habitacion con id: " + dto.getHabitacionId()));

        Reserva reserva = new Reserva(dto.getFecha_fin(), yo, habitacion);
        return toDTO(reservaRepository.save(reserva));
    }

    // Solo el estudiante que hizo la reserva puede cancelarla
    @Transactional
    public ReservaResponseDTO cancelReserva(Long id) {
        Reserva reserva = buscarReserva(id);

        if (reserva.getEstudiante() == null
                || !reserva.getEstudiante().getId().equals(usuarioService.getIdUsuarioActual())) {
            throw new AccessDeniedException("Solo el estudiante que hizo la reserva puede cancelarla");
        }
        if (reserva.getEstado() == Estado.CANCELADO) {
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya cancelada");
        } else if (reserva.getEstado() == Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya confirmada");
        }

        reserva.setEstado(Estado.CANCELADO);
        return toDTO(reservaRepository.save(reserva));
    }

    // Solo el arrendador dueño de la habitación puede confirmar
    @Transactional
    public ReservaResponseDTO confirmReserva(Long id) {
        Reserva reserva = buscarReserva(id);

        if (!reserva.getHabitacion().getArrendador().getId().equals(usuarioService.getIdUsuarioActual())) {
            throw new AccessDeniedException("Solo el dueño de la habitación puede confirmar la reserva");
        }
        if (reserva.getEstado() == Estado.CANCELADO) {
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya cancelada");
        } else if (reserva.getEstado() == Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya confirmada");
        }

        reserva.setEstado(Estado.CONFIRMADO);
        return toDTO(reservaRepository.save(reserva));
    }

    // ---------- helpers ----------

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
