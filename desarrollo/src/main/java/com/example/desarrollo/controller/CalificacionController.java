package com.example.desarrollo.controller;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.service.CalificacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

// Las calificaciones de una habitación o de un estudiante se exponen como sub-recurso de cada uno
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    @PostMapping("/calificaciones")
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(
            @Valid @RequestBody CalificacionRequestDTO dto) {
        CalificacionResponseDTO calificacion = calificacionService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(calificacion.getId())
                .toUri();
        return ResponseEntity.created(location).body(calificacion);
    }

    @GetMapping("/calificaciones/{id}")
    public ResponseEntity<CalificacionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.findById(id));
    }

    @GetMapping("/habitaciones/{habitacionId}/calificaciones")
    public ResponseEntity<Page<CalificacionResponseDTO>> getByHabitacion(
            @PathVariable Long habitacionId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(calificacionService.findByHabitacion(habitacionId, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @GetMapping("/estudiantes/{estudianteId}/calificaciones")
    public ResponseEntity<Page<CalificacionResponseDTO>> getByEstudiante(
            @PathVariable Long estudianteId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(calificacionService.findByEstudiante(estudianteId, PageRequest.of(page, size, Sort.by("id").descending())));
    }

    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'ADMIN')")
    @DeleteMapping("/calificaciones/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Long id) {
        calificacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
