package com.example.desarrollo.service;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.dto.CalificacionUpdateRequestDTO;
import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.model.Usuario;
import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;
    private final ModelMapper modelMapper;

    public CalificacionService(CalificacionRepository calificacionRepository,
                               UsuarioRepository usuarioRepository,
                               ReservaRepository reservaRepository,
                               ModelMapper modelMapper) {
        this.calificacionRepository = calificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.modelMapper = modelMapper;
    }

    // POST (Crear Calificacion)
    public CalificacionResponseDTO createCalificacion(CalificacionRequestDTO dto) {
        if (dto.getAutorId().equals(dto.getReceptorId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "No puedes calificarte a ti mismo");
        }

        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Autor no encontrado"));

        Usuario receptor = usuarioRepository.findById(dto.getReceptorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Receptor no encontrado"));

        Reserva reserva = reservaRepository.findById(dto.getReservaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada"));

        if (calificacionRepository.existsByReservaIdAndAutorId(
                dto.getReservaId(), dto.getAutorId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya calificaste esta reserva");
        }

        Calificacion calificacion = modelMapper.map(dto, Calificacion.class);
        calificacion.setAutor(autor);
        calificacion.setReceptor(receptor);
        calificacion.setReserva(reserva);

        return modelMapper.map(
                calificacionRepository.save(calificacion), CalificacionResponseDTO.class);
    }

    // GET (Obtener Calificacion)
    public CalificacionResponseDTO getById(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Calificacion no encontrada"));

        return modelMapper.map(calificacion, CalificacionResponseDTO.class);
    }

    // GET (Obtener todas las Calificaciones paginadas)
    public Page<CalificacionResponseDTO> getAll(Pageable pageable) {
        return calificacionRepository.findAll(pageable)
                .map(c -> modelMapper.map(c, CalificacionResponseDTO.class));
    }

    // PUT (Actualizar Calificacion)
    public CalificacionResponseDTO updateCalificacion(Long id, CalificacionUpdateRequestDTO dto) {
        Calificacion calificacion = calificacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Calificacion no encontrada"));

        calificacion.setPuntuacion(dto.getPuntuacion());
        calificacion.setDescripcion(dto.getDescripcion());

        return modelMapper.map(
                calificacionRepository.save(calificacion), CalificacionResponseDTO.class);
    }

    // DELETE (Eliminar Calificacion)
    public void delete(Long id) {
        if (!calificacionRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Calificacion no encontrada");
        }
        calificacionRepository.deleteById(id);
    }
}
