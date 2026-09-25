package com.example.desarrollo.service;

import com.example.desarrollo.dto.ImagenRequestDTO;
import com.example.desarrollo.dto.ImagenResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Imagen;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final HabitacionRepository habitacionRepository;
    private final UsuarioService usuarioService;

    // Guardar imagen y asociarla a la habitación (solo el dueño de la habitación)
    @Transactional
    public ImagenResponseDTO agregarImagenAHabitacion(Long habitacionId, ImagenRequestDTO requestDTO) {
        Habitacion habitacion = buscarHabitacion(habitacionId);
        validarDueno(habitacion);

        Imagen imagen = new Imagen();
        imagen.setUrl(requestDTO.getUrl());
        imagen.setHabitacion(habitacion);
        habitacion.getImagenes().add(imagen); // mantiene sincronizados ambos lados de la relación

        imagen = imagenRepository.save(imagen);
        return toDTO(imagen, habitacionId);
    }

    // Listar imágenes de una habitación (público)
    public List<ImagenResponseDTO> obtenerImagenesPorHabitacion(Long habitacionId) {
        Habitacion habitacion = buscarHabitacion(habitacionId);
        return habitacion.getImagenes().stream()
                .map(img -> toDTO(img, habitacionId))
                .toList();
    }

    // Eliminar imagen (solo el dueño de la habitación)
    @Transactional
    public void eliminarImagen(Long imagenId) {
        Imagen imagen = imagenRepository.findById(imagenId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con ID: " + imagenId));

        Habitacion habitacion = imagen.getHabitacion();
        validarDueno(habitacion);

        // Se quita de la lista primero: como Habitacion.imagenes tiene CascadeType.ALL,
        // si la imagen sigue en la lista Hibernate la "revive" y no la borra.
        habitacion.getImagenes().remove(imagen);
        imagenRepository.delete(imagen);
    }

    // ---------- helpers ----------

    private Habitacion buscarHabitacion(Long habitacionId) {
        return habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Habitación no encontrada con ID: " + habitacionId));
    }

    private void validarDueno(Habitacion habitacion) {
        Long miId = usuarioService.getIdUsuarioActual();
        if (!habitacion.getArrendador().getId().equals(miId)) {
            throw new AccessDeniedException("Solo el dueño de la habitación puede modificar sus imágenes");
        }
    }

    private ImagenResponseDTO toDTO(Imagen imagen, Long habitacionId) {
        return new ImagenResponseDTO(imagen.getId(), imagen.getUrl(), habitacionId);
    }
}
