package com.example.desarrollo.service;

import com.example.desarrollo.Events.ActualizacionPromedioEvent;
import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.access.AccessDeniedException;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;
    private final CalificacionRepository calificacionRepository;
    private final ModelMapper modelMapper;
    private final EstudianteRepository estudianteRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioService usuarioService;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Transactional
    public CalificacionResponseDTO create(CalificacionRequestDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe reserva con el ID: " + dto.getReservaId()));

        Long miId = usuarioService.getIdUsuarioActual();
        Estudiante autor = estudianteRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));


        if (reserva.getEstudiante() == null || !reserva.getEstudiante().getId().equals(autor.getId())) {
            throw new AccessDeniedException("El estudiante con ID: " + autor.getId()
                    + " no es el titular de la reserva con ID: " + reserva.getId());
        }

        if (reserva.getEstado() != Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("Solo se puede calificar una reserva confirmada");
        }

        if (calificacionRepository.existsByReservaIdAndAutorId(reserva.getId(), autor.getId())) {
            throw new ConflictException("La reserva con ID: " + reserva.getId() + " ya fue calificada");
        }

        Calificacion newCalificacion = new Calificacion();
        newCalificacion.setPuntuacion(dto.getPuntuacion());
        newCalificacion.setDescripcion(dto.getDescripcion());
        newCalificacion.setReserva(reserva);

        newCalificacion.setAutor(autor);

        Habitacion habitacion = reserva.getHabitacion();
        newCalificacion.setReceptor(habitacion);
        newCalificacion = calificacionRepository.save(newCalificacion);

        publisher.publishEvent(new ActualizacionPromedioEvent(this, habitacion.getArrendador().getId()));

        return toResponseDTO(newCalificacion);
    }

    public CalificacionResponseDTO findById(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe calificacion con el ID: " + id));
        return toResponseDTO(calificacion);
    }

    public List<CalificacionResponseDTO> findByHabitacion(Long habitacionId) {
        if (!habitacionRepository.existsById(habitacionId)) {
            throw new ResourceNotFoundException("No existe habitacion con el ID: " + habitacionId);
        }
        return calificacionRepository.findByReceptorId(habitacionId).stream()
                .map(this::toResponseDTO)
                .toList();
    }


    public List<CalificacionResponseDTO> findByEstudiante(Long estudianteId) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new ResourceNotFoundException("No existe estudiante con el ID: " + estudianteId);
        }
        return calificacionRepository.findByAutorId(estudianteId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe calificacion con el ID: " + id));

        usuarioService.validarQueSoyYo(calificacion.getAutor().getId());
        calificacionRepository.delete(calificacion);

        publisher.publishEvent(new ActualizacionPromedioEvent(this, calificacion.getReceptor().getArrendador().getId()));
    }

    // En la entidad la habitación se llama "receptor", por eso ModelMapper no llena habitacionId
    private CalificacionResponseDTO toResponseDTO(Calificacion c) {
        CalificacionResponseDTO dto = modelMapper.map(c, CalificacionResponseDTO.class);
        dto.setHabitacionId(c.getReceptor().getId());
        return dto;
    }
}
