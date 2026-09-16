package com.example.desarrollo.service;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.UniversidadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniversidadService {
    private final UniversidadRepository universidadRepository;
    private final ModelMapper modelMapper;

    public UniversidadService(UniversidadRepository universidadRepository, ModelMapper modelMapper) {
        this.universidadRepository = universidadRepository;
        this.modelMapper = modelMapper;
    }

    public UniversidadResponseDTO createUniversidad(UniversidadRequestDTO universidadRequestDTO) {
        Universidad universidad = modelMapper.map(universidadRequestDTO, Universidad.class);
        universidad = universidadRepository.save(universidad);
        return modelMapper.map(universidad, UniversidadResponseDTO.class);
    }

    public Universidad save(Universidad uni) {
        if (uni != null && uni.getNombre() != null && !uni.getNombre().isEmpty()) {
            return universidadRepository.save(uni);
        } else {
            throw new IllegalArgumentException("Nombre no puede ser nulo");
        }
    }

    public Universidad findById(Long id) {
        return universidadRepository.findById(id).orElse(null);
    }

    public List<Universidad> findAll() {
        return universidadRepository.findAll();
    }

    public void deleteById(Long id) {
        universidadRepository.deleteById(id);
    }

    public UniversidadResponseDTO findByIdDTO(Long id) {
        Universidad universidad = this.findById(id);

        if (universidad == null) {
            return null;
        }

        return modelMapper.map(universidad, UniversidadResponseDTO.class);
    }

    public List<UniversidadResponseDTO> findAllDTO() {
        List<Universidad> universidades = this.findAll();
        List<UniversidadResponseDTO> dtos = new ArrayList<>();

        for (Universidad uni : universidades) {
            UniversidadResponseDTO dto = modelMapper.map(uni, UniversidadResponseDTO.class);
            dtos.add(dto);
        }

        return dtos;
    }

}
