package com.example.desarrollo.service;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.ArrendadorResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.repository.ArrendadorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArrendadorService {
    private final ArrendadorRepository arrendadorRepository;
    private final ModelMapper modelMapper;
    private final UsuarioService usuarioService;

    @Autowired
    public ArrendadorService(ArrendadorRepository arrendadorRepository, ModelMapper modelMapper,  UsuarioService usuarioService) {
        this.arrendadorRepository = arrendadorRepository;
        this.modelMapper = modelMapper;
        this.usuarioService = usuarioService;
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
        usuarioService.validarQueSoyYo(id);
        if (!arrendadorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Arrendador no encontrado con ID: " + id);
        }
        arrendadorRepository.deleteById(id);
    }


}
