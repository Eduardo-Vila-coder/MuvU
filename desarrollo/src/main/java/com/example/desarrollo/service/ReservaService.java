package com.example.desarrollo.service;

import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ReservaRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {
    private final HabitacionRepository habitacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioService usuarioService;
    private final ReservaRepository reservaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ModelMapper modelMapper,
                          HabitacionRepository habitacionRepository, EstudianteRepository estudianteRepository, UsuarioService usuarioService) {
        this.reservaRepository=reservaRepository;
        this.modelMapper=modelMapper;
        this.habitacionRepository=habitacionRepository;
        this.estudianteRepository=estudianteRepository;
        this.usuarioService =  usuarioService;
    }

    public Reserva findById(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> findAll(){
        return reservaRepository.findAll();
    }

    public ReservaResponseDTO createReserva(ReservaRequestDTO dto) {
        Estudiante yo = estudianteRepository.findById(usuarioService.getIdUsuarioActual())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro estudiante con id: " +  usuarioService.getIdUsuarioActual()));

        Habitacion habitacion = habitacionRepository.findById(dto.getHabitacionId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro habitacion con id: " +  dto.getHabitacionId()));

        Reserva reserva = new Reserva(dto.getFecha_fin(), yo, habitacion);
        reserva = reservaRepository.save(reserva);
        return modelMapper.map(reserva, ReservaResponseDTO.class);
    }

    @Transactional
    public ReservaResponseDTO cancelReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No existe reserva con el ID:"+id));
        if(reserva.getEstado()==Estado.CANCELADO){
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya cancelada");
        }
        else if(reserva.getEstado()==Estado.CANCELADO){
            throw new ReservaInvalidStateException("No se puede cancelar una reserva ya confirmada");
        }

        reserva.setEstado(Estado.CANCELADO);
        Reserva newReserva=reservaRepository.save(reserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
    }

    @Transactional
    public ReservaResponseDTO confirmReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No existe reserva con el ID:"+id));
        if(reserva.getEstado()==Estado.CANCELADO){
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya cancelada");
        }
        else if(reserva.getEstado()==Estado.CONFIRMADO){
            throw new ReservaInvalidStateException("No se puede confirmar una reserva ya confirmada");
        }
        reserva.setEstado(Estado.CONFIRMADO);
        Reserva newReserva=reservaRepository.save(reserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
    }
}
