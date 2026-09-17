package com.example.desarrollo.service;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.ArrendadorResponseDTO;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.repository.ArrendadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArrendadorService {
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArrendadorService(ArrendadorRepository arrendadorRepository, ModelMapper modelMapper) {
        this.arrendadorRepository = arrendadorRepository;
        this.modelMapper = modelMapper;
    }

    public Arrendador findById (Long id) {
        return arrendadorRepository.findById(id).orElse(null);
    }

    public List<Arrendador> findAll() {
        return arrendadorRepository.findAll();
    }

    public Arrendador save (Arrendador arrendador) {
        return arrendadorRepository.save(arrendador);
    }

    public void deleteById(Long id) {
        arrendadorRepository.deleteById(id);
    }

    // Create (POST)
    public ArrendadorResponseDTO guardar(ArrendadorRequestDTO arrendadorRequestDTO) {
        Arrendador newArrendador = modelMapper.map(arrendadorRequestDTO, Arrendador.class);
        newArrendador = arrendadorRepository.save(newArrendador);
        return modelMapper.map(newArrendador, ArrendadorResponseDTO.class);
    }

    // Read (GET)
    public ArrendadorResponseDTO findByIdDTO(Long id) {
        Arrendador arrendador = this.findById(id);

        if (arrendador == null) {
            throw new IllegalArgumentException("El arrendador no existe");
        }
        return modelMapper.map(arrendador, ArrendadorResponseDTO.class);

    }

    public List<ArrendadorResponseDTO> findAllDTO() {
        List<Arrendador> arrendadores = this.findAll();
        List<ArrendadorResponseDTO> dtos = new ArrayList<>();
        for (Arrendador arrendador : arrendadores) {
            dtos.add(modelMapper.map(arrendador, ArrendadorResponseDTO.class));
        }
        return dtos;
    }

}
