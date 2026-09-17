package com.example.desarrollo.service;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionRequestPutDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Imagen;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ImagenRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HabitacionService {
    private final HabitacionRepository habitacionRepository;
    private final ImagenRepository imagenRepository;
    private final ModelMapper modelMapper;
    private final ArrendadorService arrendadorService;

    @Autowired
    public HabitacionService(HabitacionRepository habitacionRepository, ImagenRepository imagenRepository, ModelMapper modelMapper,  ArrendadorService arrendadorService) {
        this.habitacionRepository = habitacionRepository;
        this.imagenRepository = imagenRepository;
        this.modelMapper = modelMapper;
        this.arrendadorService = arrendadorService;
    }

    public Habitacion findById(Long id) {
        return habitacionRepository.findById(id).orElse(null);
    }

    public List<Habitacion> findAll() {
        return habitacionRepository.findAll();
    }

    public Habitacion save(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    // Delete (DELETE)
    public void deleteById(Long id) {
        if  (habitacionRepository.existsById(id)) {
            habitacionRepository.deleteById(id);
        }
        throw new ResourceNotFoundException("No existe habitacion con id: " + id);
    }

    // Create (POST)
    public HabitacionResponseDTO guardar(HabitacionRequestDTO habitacionRequestDTO) {
        Arrendador arrendador = arrendadorService.findById(habitacionRequestDTO.getArrendador().getId());
        if (arrendador == null) {
            throw new ResourceNotFoundException("Arrendador no existe");
        }

        Habitacion newHabitacion = modelMapper.map(habitacionRequestDTO, Habitacion.class);
        newHabitacion.setArrendador(arrendador);
        newHabitacion = habitacionRepository.save(newHabitacion);
        return modelMapper.map(newHabitacion, HabitacionResponseDTO.class);
    }

    // Read (GET)
    public HabitacionDetailDTO findByIdDTO(Long id) {
        Habitacion habitacion = habitacionRepository.findById(id).orElse(null);

        if (habitacion != null) {
            return modelMapper.map(habitacion, HabitacionDetailDTO.class);
        }

        return null;
    }

    public List<HabitacionResponseDTO> findAllDTO() {
        List<Habitacion> habitaciones = habitacionRepository.findAll();
        List<HabitacionResponseDTO> dtos = new ArrayList<>();
        for  (Habitacion habitacion : habitaciones) {
            dtos.add(modelMapper.map(habitacion, HabitacionResponseDTO.class));
        }
        return dtos;
    }

    // Update (PUT)
    public HabitacionResponseDTO actualizar(Long id, HabitacionRequestPutDTO habitacionRequestPutDTO) {
        Habitacion habitacionExistente = this.findById(id);
        if (habitacionExistente ==  null) {
            throw new ResourceNotFoundException("No existe habitacion con id: " + id);
        }

        habitacionExistente.setPrecio(habitacionRequestPutDTO.getPrecio());
        habitacionExistente.setDireccion(habitacionRequestPutDTO.getDireccion());
        habitacionExistente.setArea(habitacionRequestPutDTO.getArea());

        Habitacion habitacionActualizada = this.save(habitacionExistente);
        return modelMapper.map(habitacionActualizada, HabitacionResponseDTO.class);
    }

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


}
