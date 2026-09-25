package com.example.desarrollo.service;

import com.example.desarrollo.Events.NotificacionCorreoEvent;
import com.example.desarrollo.model.Arrendador;
import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.repository.ArrendadorRepository;
import com.example.desarrollo.repository.CalificacionRepository;
import com.example.desarrollo.repository.HabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArrendadorServiceTest {

    @Mock private ArrendadorRepository arrendadorRepository;
    @Mock private CalificacionRepository calificacionRepository;
    @Mock private UsuarioService usuarioService;
    @Mock private ModelMapper modelMapper;
    @Mock private HabitacionRepository habitacionRepository;
    @Mock private ApplicationEventPublisher publisher;

    @InjectMocks private ArrendadorService arrendadorService;

    private Arrendador arrendador;

    @BeforeEach
    void setUp() {
        arrendador = new Arrendador();
        arrendador.setId(2L);
        when(arrendadorRepository.findById(2L)).thenReturn(Optional.of(arrendador));
    }

    @Test
    void actualizarPromedio_calculaPromedioRedondeadoYTotal() {
        when(calificacionRepository.findByReceptorArrendadorId(2L))
                .thenReturn(List.of(calificacion(5), calificacion(4), calificacion(4)));

        arrendadorService.actualizarPromedio(2L);

        assertEquals(4.3, arrendador.getPuntajePromedio());
        assertEquals(3L, arrendador.getTotalCalificaciones());
    }

    @Test
    void actualizarPromedio_sinCalificaciones_quedaEnCero() {
        when(calificacionRepository.findByReceptorArrendadorId(2L)).thenReturn(List.of());

        arrendadorService.actualizarPromedio(2L);

        assertEquals(0.0, arrendador.getPuntajePromedio());
        assertEquals(0L, arrendador.getTotalCalificaciones());
    }

    @Test
    void verificar_marcaAlArrendadorYLeEnviaCorreo() {
        arrendadorService.verificar(2L);

        assertTrue(arrendador.getVerificado());
        verify(publisher).publishEvent(any(NotificacionCorreoEvent.class));
    }

    @Test
    void actualizarCantidadHabitaciones_guardaElConteo() {
        when(habitacionRepository.countByArrendadorId(2L)).thenReturn(3L);

        arrendadorService.actualizarCantidadHabitaciones(2L);

        assertEquals(3L, arrendador.getCantidadHabitaciones());
    }

    private Calificacion calificacion(int puntuacion) {
        Calificacion c = new Calificacion();
        c.setPuntuacion(puntuacion);
        return c;
    }
}
