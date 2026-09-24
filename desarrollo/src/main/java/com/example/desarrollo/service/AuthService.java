package com.example.desarrollo.service;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.Logueo.LoginRequestDTO;
import com.example.desarrollo.dto.Logueo.TokenResponseDTO;
import com.example.desarrollo.exceptions.ConflictException;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.*;
import com.example.desarrollo.repository.ArrendadorRepository;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.UniversidadRepository;
import com.example.desarrollo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final ArrendadorRepository arrendadorRepository;
    private final UniversidadRepository universidadRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public TokenResponseDTO registerEstudiante(EstudianteRequestDTO dto) {
        validarCorreoLibre(dto.getCorreo());
        Universidad uni = universidadRepository.findById(dto.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        Estudiante e = new Estudiante();
        e.setNombre(dto.getNombre());
        e.setCorreo(dto.getCorreo());
        e.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        e.setRol(Rol.ESTUDIANTE);
        e.setUniversidad(uni);

        estudianteRepository.save(e);
        return new TokenResponseDTO(jwtService.generateToken(e), e.getRol().name());
    }

    @Transactional
    public TokenResponseDTO registerArrendador(ArrendadorRequestDTO dto) {
        validarCorreoLibre(dto.getCorreo());

        Arrendador a = new Arrendador();
        a.setNombre(dto.getNombre());
        a.setCorreo(dto.getCorreo());
        a.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        a.setRol(Rol.ARRENDADOR);

        arrendadorRepository.save(a);
        return new TokenResponseDTO(jwtService.generateToken(a), a.getRol().name());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        // Lanza BadCredentialsException si el correo o la contraseña están mal
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getCorreo(), dto.getContrasena()));

        Usuario u = usuarioRepository.findByCorreo(dto.getCorreo()).orElseThrow();
        return new TokenResponseDTO(jwtService.generateToken(u), u.getRol().name());
    }

    private void validarCorreoLibre(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new ConflictException("El correo " + correo + " ya está registrado");
        }
    }
}
