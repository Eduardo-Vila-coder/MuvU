package com.example.desarrollo.service;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;


    public CalificacionResponseDTO create(CalificacionRequestDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe reserva con el ID: " + dto.getReservaId()));

        Estudiante autor = estudianteRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe estudiante con el ID: " + dto.getAutorId()));

        if (reserva.getEstudiante() == null || !reserva.getEstudiante().getId().equals(autor.getId())) {
            throw new IllegalArgumentException("El estudiante con ID: " + autor.getId()
                    + " no es el titular de la reserva con ID: " + reserva.getId());
        }

        if (reserva.getEstado() != Estado.CONFIRMADO) {
            throw new ReservaInvalidStateException("Solo se puede calificar una reserva confirmada");
        }

        if (calificacionRepository.existsByReservaIdAndAutorId(reserva.getId(), autor.getId())) {
            throw new ConflictException("La reserva con ID: " + reserva.getId() + " ya fue calificada");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setPuntuacion(dto.getPuntuacion());
        calificacion.setDescripcion(dto.getDescripcion());
        calificacion.setAutor(autor);
        calificacion.setReserva(reserva);
        calificacion.setReceptor(reserva.getHabitacion()); // la habitacion sale de la reserva, no del request

        return toResponseDTO(calificacionRepository.save(calificacion));
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

    public void deleteById(Long id) {
        if (!calificacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe calificacion con el ID: " + id);
        }
        calificacionRepository.deleteById(id);
    }

    private CalificacionResponseDTO toResponseDTO(Calificacion c) {
        return new CalificacionResponseDTO(
                c.getId(),
                c.getPuntuacion(),
                c.getDescripcion(),
                c.getAutor().getId(),
                c.getAutor().getNombre(),
                c.getReceptor().getId(),
                c.getReserva().getId()
        );
    }
}
