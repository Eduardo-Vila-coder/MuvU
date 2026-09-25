package com.example.desarrollo.controller;

import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.EstudianteResponseDTO;
import com.example.desarrollo.service.EstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.desarrollo.dto.EstudiantePerfilDTO;
import com.example.desarrollo.dto.EstudianteUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {
    private final EstudianteService estudianteService;


    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.getById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<EstudianteResponseDTO>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(estudianteService.getAll(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteUpdateRequestDTO dto) {
        return ResponseEntity.ok(estudianteService.updateEstudiante(id, dto));
    }

    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @GetMapping("/{id}/perfil")
    public ResponseEntity<EstudiantePerfilDTO> getPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.getPerfil(id));
    }
}
