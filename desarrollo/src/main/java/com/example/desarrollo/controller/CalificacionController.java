package com.example.desarrollo.controller;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.dto.CalificacionUpdateRequestDTO;
import com.example.desarrollo.service.CalificacionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(
            @Valid @RequestBody CalificacionRequestDTO dto) {
        return new ResponseEntity<>(calificacionService.createCalificacion(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CalificacionResponseDTO>> getAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(calificacionService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> updateCalificacion(
            @PathVariable Long id,
            @Valid @RequestBody CalificacionUpdateRequestDTO dto) {
        return ResponseEntity.ok(calificacionService.updateCalificacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        calificacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}