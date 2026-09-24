package com.example.desarrollo.service;

import com.example.desarrollo.dto.ImagenRequestDTO;
import com.example.desarrollo.dto.ImagenResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Imagen;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final HabitacionRepository habitacionRepository;
    private final ModelMapper modelMapper;

    // Guardar imagen y asociarla directamente a la habitación
    @Transactional
    public ImagenResponseDTO agregarImagenAHabitacion(Long habitacionId, ImagenRequestDTO requestDTO) {
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + habitacionId));

        Imagen imagen = new Imagen();
        imagen.setUrl(requestDTO.getUrl());
        imagen.setHabitacion(habitacion);

        imagen = imagenRepository.save(imagen);

        ImagenResponseDTO responseDTO = modelMapper.map(imagen, ImagenResponseDTO.class);
        responseDTO.setHabitacionId(habitacion.getId());
        return responseDTO;
    }

    // Listar imágenes de una habitación específica
    public List<ImagenResponseDTO> obtenerImagenesPorHabitacion(Long habitacionId) {
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + habitacionId));

        return habitacion.getImagenes().stream()
                .map(img -> {
                    ImagenResponseDTO dto = modelMapper.map(img, ImagenResponseDTO.class);
                    dto.setHabitacionId(habitacionId);
                    return dto;
                })
                .toList();
    }

    // Eliminar imagen por ID
    @Transactional
    public void eliminarImagen(Long imagenId) {
        if (!imagenRepository.existsById(imagenId)) {
            throw new ResourceNotFoundException("Imagen no encontrada con ID: " + imagenId);
        }
        imagenRepository.deleteById(imagenId);
    }
}