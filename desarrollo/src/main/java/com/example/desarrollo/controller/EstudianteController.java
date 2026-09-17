package com.example.desarrollo.controller;

import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.EstudianteResponseDTO;
import com.example.desarrollo.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.desarrollo.dto.EstudianteUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> createEstudiante(@Valid @RequestBody EstudianteRequestDTO dto) {
        return new ResponseEntity<>(estudianteService.createEstudiante(dto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<Page<EstudianteResponseDTO>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(estudianteService.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteUpdateRequestDTO dto) {
        return ResponseEntity.ok(estudianteService.updateEstudiante(id, dto));
    }

    @GetMapping("/{id}/perfil")
    public ResponseEntity<EstudianteResponseDTO> getPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.getPerfil(id));
    }
}
