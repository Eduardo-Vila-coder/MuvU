package com.example.desarrollo.service;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.ArrendadorResponseDTO;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.repository.ArrendadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArrendadorService {
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ArrendadorService(ArrendadorRepository arrendadorRepository, ModelMapper modelMapper) {
        this.arrendadorRepository = arrendadorRepository;
        this.modelMapper = modelMapper;
    }

    // Create (POST)
    public ArrendadorResponseDTO save(ArrendadorRequestDTO arrendadorRequestDTO) {
        if (arrendadorRequestDTO != null
            && arrendadorRequestDTO.getNombre() != null && !arrendadorRequestDTO.getNombre().isEmpty()
            && arrendadorRequestDTO.getCorreo() != null && !arrendadorRequestDTO.getCorreo().isEmpty()
            && arrendadorRequestDTO.getContrasena() != null && !arrendadorRequestDTO.getContrasena().isEmpty()) {

            Arrendador newArrendador = modelMapper.map(arrendadorRequestDTO, Arrendador.class);
            newArrendador = arrendadorRepository.save(newArrendador);
            return modelMapper.map(newArrendador, ArrendadorResponseDTO.class);
        } else {
            throw new IllegalArgumentException("El nombre, correo o constrasena de un Arrendador no pueden ser nulos ni vacios");
        }
    }

    // Read (GET)
    public ArrendadorResponseDTO findById(Long id) {
        Arrendador arrendador = arrendadorRepository.findById(id).orElse(null);

        if (arrendador != null) {
            return modelMapper.map(arrendador, ArrendadorResponseDTO.class);
        }

        return null;
    }

    // Update (PUT) - Que se actualice la foto del DNI

    // (PATCH)

    // Delete (DELETE)
    public void deleteById(Long id) {
        arrendadorRepository.deleteById(id);
    }
}
