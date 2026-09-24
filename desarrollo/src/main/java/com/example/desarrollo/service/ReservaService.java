package com.example.desarrollo.service;

import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ReservaInvalidStateException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ModelMapper modelMapper){
        this.reservaRepository=reservaRepository;
        this.modelMapper=modelMapper;
    }

    public Reserva findById(Long id){
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> findAll(){
        return reservaRepository.findAll();
    }

    public ReservaResponseDTO createReserva(ReservaRequestDTO reservaRequestDTO){
        Reserva newReserva=modelMapper.map(reservaRequestDTO, Reserva.class);
        newReserva= reservaRepository.save(newReserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
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
