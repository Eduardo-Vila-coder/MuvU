package com.example.desarrollo.controller;

import com.example.desarrollo.dto.*;
import com.example.desarrollo.dto.Logueo.LoginRequestDTO;
import com.example.desarrollo.dto.Logueo.TokenResponseDTO;
import com.example.desarrollo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/estudiante")
    public ResponseEntity<TokenResponseDTO> registerEstudiante(@Valid @RequestBody EstudianteRequestDTO dto) {
        return new ResponseEntity<>(authService.registerEstudiante(dto), HttpStatus.CREATED);
    }

    @PostMapping("/register/arrendador")
    public ResponseEntity<TokenResponseDTO> registerArrendador(@Valid @RequestBody ArrendadorRequestDTO dto) {
        return new ResponseEntity<>(authService.registerArrendador(dto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}