package com.example.desarrollo.service;


import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

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

}
