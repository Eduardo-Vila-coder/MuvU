package com.example.desarrollo.controller;

import com.example.desarrollo.dto.*;
import com.example.desarrollo.dto.Logueo.ForgotPasswordRequestDTO;
import com.example.desarrollo.dto.Logueo.LoginRequestDTO;
import com.example.desarrollo.dto.Logueo.RefreshTokenRequestDTO;
import com.example.desarrollo.dto.Logueo.ResetPasswordRequestDTO;
import com.example.desarrollo.dto.Logueo.TokenResponseDTO;
import com.example.desarrollo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/estudiante")
    public ResponseEntity<TokenResponseDTO> registerEstudiante(@Valid @RequestBody EstudianteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerEstudiante(dto));
    }

    @PostMapping("/register/arrendador")
    public ResponseEntity<TokenResponseDTO> registerArrendador(@Valid @RequestBody ArrendadorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerArrendador(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // Cambia un refresh token válido por un access token nuevo (sin volver a pedir la contraseña)
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    // Siempre 204, exista o no el correo, para no revelar qué correos están registrados
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO dto) {
        authService.forgotPassword(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        authService.resetPassword(dto);
        return ResponseEntity.noContent().build();
    }
}