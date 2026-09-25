package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    private final ApplicationEventPublisher publisher;

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
        log.info("Estudiante {} registrado en la universidad {}", e.getId(), uni.getId());
        enviarBienvenida(e, "Ya puedes buscar y reservar habitaciones cerca de tu universidad.");
        return new TokenResponseDTO(jwtService.generateToken(e), e.getRol().name(), e.getId());
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
        log.info("Arrendador {} registrado, pendiente de verificación", a.getId());
        enviarBienvenida(a, "Un administrador revisará tu cuenta; cuando esté verificada podrás publicar tus habitaciones.");
        return new TokenResponseDTO(jwtService.generateToken(a), a.getRol().name(), a.getId());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        // Lanza BadCredentialsException si el correo o la contraseña están mal
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getCorreo(), dto.getContrasena()));

        Usuario u = usuarioRepository.findByCorreo(dto.getCorreo()).orElseThrow();
        log.info("Usuario {} inició sesión con rol {}", u.getId(), u.getRol());
        return new TokenResponseDTO(jwtService.generateToken(u), u.getRol().name(), u.getId());
    }

    private void enviarBienvenida(Usuario usuario, String mensaje) {
        publisher.publishEvent(new NotificacionCorreoEvent(this, Mail.para(usuario.getCorreo(),
                "¡Bienvenido a MuvU!",
                "Hola " + usuario.getNombre() + ", tu cuenta fue creada con éxito. " + mensaje)));
    }

    private void validarCorreoLibre(String correo) {
        if (usuarioRepository.existsByCorreo(correo)) {
            log.warn("Intento de registro con un correo ya registrado");
            throw new DuplicateResourceException("El correo " + correo + " ya está registrado");
        }
    }
}
