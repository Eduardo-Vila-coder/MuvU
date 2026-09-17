package com.example.desarrollo.service;

import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Reserva;
import com.example.desarrollo.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final ModelMapper modelMapper;
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

    public void deleteById(Long id){
        if (reservaRepository.existsById(id)){
            reservaRepository.deleteById(id);
        }
        throw new  ResourceNotFoundException("No existe reserva con id: " + id);
    }

    public ReservaResponseDTO createReserva(ReservaRequestDTO reservaRequestDTO){
        Reserva newReserva=modelMapper.map(reservaRequestDTO, Reserva.class);
        newReserva= reservaRepository.save(newReserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
    }
    @Transactional
    public ReservaResponseDTO cancelReserva(Long id){
        Reserva reserva=reservaRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No existe reserva con el ID:"+id));
        reserva.setEstado(Estado.CANCELADO);
        Reserva newReserva=reservaRepository.save(reserva);
        return modelMapper.map(newReserva,ReservaResponseDTO.class);
    }
}
