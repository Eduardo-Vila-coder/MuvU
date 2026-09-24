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
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class HabitacionService {

    private final GoogleMapsService googleMapsService;
    private final HabitacionRepository habitacionRepository;
    private final ImagenRepository imagenRepository;
    private final ModelMapper modelMapper;

    // Create (POST)
    @Transactional
    public HabitacionResponseDTO save(HabitacionRequestDTO habitacionRequestDTO) {
        Habitacion newHabitacion = modelMapper.map(habitacionRequestDTO, Habitacion.class);
        LatLng coords = googleMapsService.obtenerCoordenadas(newHabitacion.getDireccion());
        newHabitacion.setLatitud(coords.lat);
        newHabitacion.setLongitud(coords.lng);
        newHabitacion = habitacionRepository.save(newHabitacion);
        return modelMapper.map(newHabitacion, HabitacionResponseDTO.class);
    }

    // Read (GET)
    public HabitacionDetailDTO findById(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id).orElse(null);

        if (habitacion != null) {
            return modelMapper.map(habitacion, HabitacionDetailDTO.class);
        }

        return null;
    }

    // Read all paginado (GET)
    public Page<HabitacionResponseDTO> findAll(Pageable pageable) {
        return habitacionRepository.findAll(pageable)
                .map(h -> modelMapper.map(h, HabitacionResponseDTO.class));
    }

    // (PATCH)
    @Transactional
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
    @Transactional
    public void deleteById(Long id) {
        habitacionRepository.deleteById(id);
    }
}
