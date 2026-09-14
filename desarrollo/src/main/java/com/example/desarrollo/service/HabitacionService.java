package com.example.desarrollo.service;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.repository.HabitacionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HabitacionService {
    @Autowired
    private final HabitacionRepository habitacionRepository;
    private final ModelMapper modelMapper;

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

    // PATCH

    // Delete (DELETE)
    public void deleteById(Long id) {
        habitacionRepository.deleteById(id);
    }
}
