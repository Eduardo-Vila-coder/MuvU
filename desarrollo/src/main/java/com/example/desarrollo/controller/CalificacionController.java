package com.example.desarrollo.controller;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.CalificacionResponseDTO;
import com.example.desarrollo.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(
            @Valid @RequestBody CalificacionRequestDTO dto) {
        return new ResponseEntity<>(calificacionService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(calificacionService.findById(id));
    }

    @GetMapping("/habitacion/{habitacionId}")
    public ResponseEntity<List<CalificacionResponseDTO>> getByHabitacion(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(calificacionService.findByHabitacion(habitacionId));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<CalificacionResponseDTO>> getByEstudiante(@PathVariable Long estudianteId) {
        return ResponseEntity.ok(calificacionService.findByEstudiante(estudianteId));
    }

    @PreAuthorize("hasAnyAuthority('ESTUDIANTE', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Long id) {
        calificacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
