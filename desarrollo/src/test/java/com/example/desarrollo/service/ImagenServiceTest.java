package com.example.desarrollo.service;

import com.example.desarrollo.dto.ImagenRequestDTO;
import com.example.desarrollo.dto.ImagenResponseDTO;
import com.example.desarrollo.exceptions.ForbiddenException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.model.Habitacion;
import com.example.desarrollo.model.Imagen;
import com.example.desarrollo.repository.HabitacionRepository;
import com.example.desarrollo.repository.ImagenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImagenServiceTest {

    @Mock private ImagenRepository imagenRepository;
    @Mock private HabitacionRepository habitacionRepository;
    @Mock private UsuarioService usuarioService;

    @InjectMocks private ImagenService imagenService;

    private Habitacion habitacion;

    @BeforeEach
    void setUp() {
        Arrendador arrendador = new Arrendador();
        arrendador.setId(2L);
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setArrendador(arrendador);
    }

    @Test
    void agregarImagen_porDueno_laAsociaALaHabitacion() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(usuarioService.getIdUsuarioActual()).thenReturn(2L);
        when(imagenRepository.save(any(Imagen.class))).thenAnswer(inv -> {
            Imagen imagen = inv.getArgument(0);
            imagen.setId(7L);
            return imagen;
        });

        ImagenResponseDTO respuesta = imagenService.agregarImagenAHabitacion(1L, new ImagenRequestDTO("https://muvu.com/1.jpg"));

        assertEquals(7L, respuesta.getId());
        assertEquals(1L, respuesta.getHabitacionId());
        assertEquals(1, habitacion.getImagenes().size());
    }

    @Test
    void agregarImagen_porOtroUsuario_lanzaForbidden() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));
        when(usuarioService.getIdUsuarioActual()).thenReturn(99L);

        assertThrows(ForbiddenException.class,
                () -> imagenService.agregarImagenAHabitacion(1L, new ImagenRequestDTO("https://muvu.com/1.jpg")));
        verify(imagenRepository, never()).save(any());
    }

    @Test
    void obtenerImagenes_devuelveLasDeLaHabitacion() {
        habitacion.agregarImagen(imagen());
        when(habitacionRepository.findById(1L)).thenReturn(Optional.of(habitacion));

        assertEquals(1, imagenService.obtenerImagenesPorHabitacion(1L).size());
    }

    @Test
    void obtenerImagenes_habitacionInexistente_lanzaNotFound() {
        when(habitacionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> imagenService.obtenerImagenesPorHabitacion(1L));
    }

    @Test
    void eliminarImagen_porDueno_laQuitaDeLaHabitacion() {
        Imagen imagen = imagen();
        habitacion.agregarImagen(imagen);
        when(imagenRepository.findById(7L)).thenReturn(Optional.of(imagen));
        when(usuarioService.getIdUsuarioActual()).thenReturn(2L);

        imagenService.eliminarImagen(7L);

        assertTrue(habitacion.getImagenes().isEmpty());
    }

    @Test
    void eliminarImagen_inexistente_lanzaNotFound() {
        when(imagenRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> imagenService.eliminarImagen(7L));
    }

    private Imagen imagen() {
        Imagen imagen = new Imagen();
        imagen.setId(7L);
        imagen.setUrl("https://muvu.com/1.jpg");
        return imagen;
    }
}
