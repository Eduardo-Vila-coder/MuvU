package com.example.desarrollo.service;

import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.model.Reserva;
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

}
