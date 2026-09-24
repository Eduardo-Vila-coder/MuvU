package com.example.desarrollo.service;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final ModelMapper modelMapper;
    private final EstudianteRepository estudianteRepository;
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioService usuarioService;


    public CalificacionResponseDTO create(CalificacionRequestDTO dto) {
        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new ResourceNotFoundException("No existe reserva con el ID: " + dto.getReservaId()));

        Long miId = usuarioService.getIdUsuarioActual();
        Estudiante autor = estudianteRepository.findById(miId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));


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

        Calificacion newCalificacion = modelMapper.map(dto, Calificacion.class);

        newCalificacion.setReserva(reserva);

        newCalificacion.setAutor(autor);

        Habitacion habitacion = habitacionRepository.findById(dto.getReceptorId())
                .orElseThrow(() -> new ResourceNotFoundException("La habitacion a calificar no existe"));
        newCalificacion.setReceptor(habitacion);

        newCalificacion = calificacionRepository.save(newCalificacion);
        return modelMapper.map(newCalificacion, CalificacionResponseDTO.class);
    }

    public CalificacionResponseDTO findById(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe calificacion con el ID: " + id));
        return modelMapper.map(calificacion, CalificacionResponseDTO.class);
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
        return modelMapper.map(c, CalificacionResponseDTO.class);
    }
}
