package com.example.desarrollo.service;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.UniversidadRepository;
import com.google.maps.model.LatLng;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniversidadServiceTest {

    @Mock private GoogleMapsService googleMapsService;
    @Mock private UniversidadRepository universidadRepository;
    @Spy private ModelMapper modelMapper = new ModelMapper();

    @InjectMocks private UniversidadService universidadService;

    @Test
    void crear_geolocalizaPorDireccion() {
        when(googleMapsService.obtenerCoordenadas("Jr. Medrano Silva 165")).thenReturn(new LatLng(-12.13, -77.02));
        when(universidadRepository.save(any(Universidad.class))).thenAnswer(inv -> {
            Universidad u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UniversidadResponseDTO respuesta = universidadService.createUniversidad(
                new UniversidadRequestDTO("UTEC", "Jr. Medrano Silva 165"));

        assertEquals(1L, respuesta.getId());
        assertEquals(-12.13, respuesta.getLatitud());
    }

    @Test
    void crear_sinDireccion_buscaPorNombre() {
        when(googleMapsService.obtenerCoordenadas("UTEC")).thenReturn(new LatLng(-12.13, -77.02));
        when(universidadRepository.save(any(Universidad.class))).thenAnswer(inv -> inv.getArgument(0));

        universidadService.createUniversidad(new UniversidadRequestDTO("UTEC", " "));

        verify(googleMapsService).obtenerCoordenadas("UTEC");
    }

    @Test
    void findAll_devuelveTodas() {
        when(universidadRepository.findAll()).thenReturn(List.of(universidad(), universidad()));

        assertEquals(2, universidadService.findAllDTO().size());
    }

    @Test
    void findById_existente_devuelveDTO() {
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(universidad()));

        assertEquals("UTEC", universidadService.findByIdDTO(1L).getNombre());
    }

    @Test
    void findById_inexistente_lanzaNotFound() {
        when(universidadRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> universidadService.findByIdDTO(9L));
    }

    @Test
    void eliminar_existente_laBorra() {
        when(universidadRepository.existsById(1L)).thenReturn(true);

        universidadService.deleteById(1L);

        verify(universidadRepository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(universidadRepository.existsById(9L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> universidadService.deleteById(9L));
        verify(universidadRepository, never()).deleteById(any());
    }

    private Universidad universidad() {
        Universidad u = new Universidad();
        u.setId(1L);
        u.setNombre("UTEC");
        u.setDireccion("Jr. Medrano Silva 165");
        return u;
    }
}
