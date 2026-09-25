package com.example.desarrollo.controller;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<List<CalificacionResponseDTO>> getByHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(calificacionService.findByHabitacion(habitacionId));
    }

    @GetMapping("/estudiantes/{estudianteId}/calificaciones")
    public ResponseEntity<List<CalificacionResponseDTO>> getByEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(calificacionService.findByEstudiante(estudianteId));
    }

    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'ADMIN')")
    @DeleteMapping("/calificaciones/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Long id) {
        calificacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
