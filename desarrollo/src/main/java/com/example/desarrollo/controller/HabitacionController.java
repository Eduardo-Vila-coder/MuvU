package com.example.desarrollo.controller;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.service.HabitacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/habitaciones")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

    // Habitaciones dentro de un radio (km) alrededor de una universidad
    @GetMapping("/cercanas")
    public ResponseEntity<Page<HabitacionResponseDTO>> getHabitacionesCercanas(
            @RequestParam @Positive(message = "El ID de la universidad debe ser positivo") Long universidadId,
            @RequestParam(defaultValue = "5.0")
            @Positive(message = "El radio debe ser mayor a cero")
            @DecimalMax(value = "50.0", message = "El radio no puede superar 50 km") Double radioKm,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "El tamaño de página debe ser al menos 1")
            @Max(value = 100, message = "El tamaño de página no puede superar 100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(habitacionService.findCercanas(universidadId, radioKm, pageable));
    }

    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> createHabitacion(
            @Valid @RequestBody HabitacionRequestDTO habitacionRequestDTO) {
        HabitacionResponseDTO savedHabitacion = habitacionService.save(habitacionRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedHabitacion.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedHabitacion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDetailDTO> getHabitacionById(@PathVariable Long id) {
        return ResponseEntity.ok(habitacionService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<HabitacionResponseDTO>> getAllHabitaciones(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página no puede ser negativa") int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "El tamaño de página debe ser al menos 1")
            @Max(value = 100, message = "El tamaño de página no puede superar 100") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(habitacionService.findAll(pageable));
    }

    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<HabitacionResponseDTO> updateHabitacion(
            @PathVariable Long id, @Valid @RequestBody HabitacionRequestDTO dto) {
        return ResponseEntity.ok(habitacionService.update(id, dto));
    }

    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable Long id) {
        habitacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}