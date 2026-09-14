package com.example.desarrollo.service;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Imagen;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ImagenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitacionService {
    private final HabitacionRepository habitacionRepository;
    private final ImagenRepository imagenRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HabitacionService(HabitacionRepository habitacionRepository, ImagenRepository imagenRepository, ModelMapper modelMapper) {
        this.habitacionRepository = habitacionRepository;
        this.imagenRepository = imagenRepository;
        this.modelMapper = modelMapper;
    }

    // Create (POST)
    public HabitacionResponseDTO save(HabitacionRequestDTO habitacionRequestDTO) {
        if (habitacionRequestDTO != null
            && habitacionRequestDTO.getDireccion() != null && !habitacionRequestDTO.getDireccion().isEmpty()
            && habitacionRequestDTO.getArea() != null
            && habitacionRequestDTO.getArrendador() != null) {
            Habitacion newHabitacion = modelMapper.map(habitacionRequestDTO, Habitacion.class);
            newHabitacion = habitacionRepository.save(newHabitacion);
            return modelMapper.map(newHabitacion, HabitacionResponseDTO.class);
        } else {
            throw new IllegalArgumentException("La direccion, el area y el arrendador de una Habitacion no pueden ser nulos ni vacios");
        }
    }

    // Read (GET)
    public HabitacionDetailDTO findById(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id).orElse(null);

        if (habitacion != null) {
            return modelMapper.map(habitacion, HabitacionDetailDTO.class);
        }

        return null;
    }

    // Update (PUT)

    // (PATCH)
    public HabitacionDetailDTO addImagen(Long habitacionId, Long imagenId) {
        Imagen imagen = imagenRepository.findById(imagenId).orElse(null);

        if (imagen == null) {
            throw new ResourceNotFoundException("No fue encontrada la imagen con id: " + imagenId);
        }

        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("No fue encontrada la habitacion con id: " + habitacionId));


        if (habitacion.getImagenes().contains(imagen)) {
            throw new ConflictException("La imagen con id: " + imagenId + " ya esta asociada a la habitacion con id: " + habitacionId);
        }

        habitacion.getImagenes().add(imagen);

        habitacion = habitacionRepository.save(habitacion);

        return modelMapper.map(habitacion, HabitacionDetailDTO.class);
    }

    // Delete (DELETE)
    public void deleteById(Long id) {
        habitacionRepository.deleteById(id);
    }
}
