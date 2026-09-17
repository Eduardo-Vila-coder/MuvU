package com.example.desarrollo.service;


import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public Calificacion findById(Long id) {
        return calificacionRepository.findById(id).orElse(null);
    }

    public List<Calificacion> findAll() {
        return calificacionRepository.findAll();
    }

    public void save(Calificacion calificacion) {
        calificacionRepository.save(calificacion);
    }

    public void deleteById(Long id) {
        Calificacion calificacion = calificacionRepository.findById(id).orElse(null);
        if (calificacionRepository.existsById(id)) {
            calificacionRepository.delete(calificacion);
        }
        throw new ResourceNotFoundException("No existe calificacion con id: " + id);


    }
}
