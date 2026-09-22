package com.example.desarrollo.controller;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/habitacion")
@RequiredArgsConstructor
public class HabitacionController {

    private final HabitacionService habitacionService;

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
        HabitacionDetailDTO habitacionDetailDTO = habitacionService.findById(id);

        if (habitacionDetailDTO != null) {
            return ResponseEntity.ok(habitacionDetailDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Ruta RESTful mejorada: /habitacion/{id}/imagenes/{imagenId}
    @PatchMapping("/{id}/imagenes/{imagenId}")
    public ResponseEntity<HabitacionDetailDTO> addImagen(
            @PathVariable Long id,
            @PathVariable Long imagenId) {
        HabitacionDetailDTO habitacionDetailDTO = habitacionService.addImagen(id, imagenId);
        return ResponseEntity.ok(habitacionDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable Long id) {
        habitacionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}