package com.example.desarrollo.service;

import com.example.desarrollo.Events.NotificacionCorreoEvent;
import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.Logueo.LoginRequestDTO;
import com.example.desarrollo.dto.Logueo.TokenResponseDTO;
import com.example.desarrollo.exceptions.DuplicateResourceException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.ArrendadorRepository;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.UniversidadRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private EstudianteRepository estudianteRepository;
    @Mock private ArrendadorRepository arrendadorRepository;
    @Mock private UniversidadRepository universidadRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private ApplicationEventPublisher publisher;

    @InjectMocks private AuthService authService;

    @Test
    void registrarEstudiante_guardaClaveCifradaYDevuelveToken() {
        Universidad utec = new Universidad();
        utec.setId(1L);
        when(usuarioRepository.existsByCorreo("ana@utec.edu.pe")).thenReturn(false);
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(utec));
        when(passwordEncoder.encode("Clave123!")).thenReturn("hash");
        when(jwtService.generateToken(any(Estudiante.class))).thenReturn("jwt");

        TokenResponseDTO respuesta = authService.registerEstudiante(
                new EstudianteRequestDTO("Ana", "ana@utec.edu.pe", "Clave123!", 1L));

        ArgumentCaptor<Estudiante> captor = ArgumentCaptor.forClass(Estudiante.class);
        verify(estudianteRepository).save(captor.capture());
        assertEquals("hash", captor.getValue().getContrasena());
        assertEquals(utec, captor.getValue().getUniversidad());
        assertEquals("jwt", respuesta.token());
        assertEquals("ESTUDIANTE", respuesta.rol());
        verify(publisher).publishEvent(any(NotificacionCorreoEvent.class));
    }

    @Test
    void registrarEstudiante_conCorreoRepetido_lanzaDuplicate() {
        when(usuarioRepository.existsByCorreo("ana@utec.edu.pe")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.registerEstudiante(
                new EstudianteRequestDTO("Ana", "ana@utec.edu.pe", "Clave123!", 1L)));
        verify(estudianteRepository, never()).save(any());
    }

    @Test
    void registrarEstudiante_conUniversidadInexistente_lanzaNotFound() {
        when(usuarioRepository.existsByCorreo("ana@utec.edu.pe")).thenReturn(false);
        when(universidadRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.registerEstudiante(
                new EstudianteRequestDTO("Ana", "ana@utec.edu.pe", "Clave123!", 9L)));
    }

    @Test
    void registrarArrendador_quedaSinVerificarYRecibeBienvenida() {
        when(usuarioRepository.existsByCorreo("rosa@muvu.com")).thenReturn(false);
        when(passwordEncoder.encode("Clave123!")).thenReturn("hash");
        when(jwtService.generateToken(any(Arrendador.class))).thenReturn("jwt");

        TokenResponseDTO respuesta = authService.registerArrendador(
                new ArrendadorRequestDTO("Rosa", "rosa@muvu.com", "Clave123!"));

        ArgumentCaptor<Arrendador> captor = ArgumentCaptor.forClass(Arrendador.class);
        verify(arrendadorRepository).save(captor.capture());
        assertFalse(captor.getValue().getVerificado());
        assertEquals("ARRENDADOR", respuesta.rol());
        verify(publisher).publishEvent(any(NotificacionCorreoEvent.class));
    }

    @Test
    void login_conCredencialesValidas_devuelveToken() {
        Usuario ana = new Estudiante();
        ana.setId(3L);
        ana.setRol(Rol.ESTUDIANTE);
        when(usuarioRepository.findByCorreo("ana@utec.edu.pe")).thenReturn(Optional.of(ana));
        when(jwtService.generateToken(ana)).thenReturn("jwt");

        TokenResponseDTO respuesta = authService.login(login("ana@utec.edu.pe", "Clave123!"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("jwt", respuesta.token());
        assertEquals(3L, respuesta.id());
    }

    @Test
    void login_conClaveIncorrecta_propagaBadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("mal"));

        assertThrows(BadCredentialsException.class, () -> authService.login(login("ana@utec.edu.pe", "otra")));
        verify(jwtService, never()).generateToken(any());
    }

    private LoginRequestDTO login(String correo, String contrasena) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setCorreo(correo);
        dto.setContrasena(contrasena);
        return dto;
    }
}
