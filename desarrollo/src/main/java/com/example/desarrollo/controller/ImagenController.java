package com.example.desarrollo.controller;

import com.example.desarrollo.dto.ImagenRequestDTO;
import com.example.desarrollo.dto.ImagenResponseDTO;
import com.example.desarrollo.service.ImagenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    // POST: /habitacion/1/imagenes
    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @PostMapping("/habitacion/{habitacionId}/imagenes")
    public ResponseEntity<ImagenResponseDTO> agregarImagen(
            @PathVariable Long habitacionId,
            @Valid @RequestBody ImagenRequestDTO requestDTO) {
        ImagenResponseDTO nuevaImagen = imagenService.agregarImagenAHabitacion(habitacionId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaImagen);
    }

    // GET: /habitacion/1/imagenes
    @GetMapping("/habitacion/{habitacionId}/imagenes")
    public ResponseEntity<List<ImagenResponseDTO>> obtenerImagenes(@PathVariable Long habitacionId) {
        return ResponseEntity.ok(imagenService.obtenerImagenesPorHabitacion(habitacionId));
    }

    // DELETE: /imagenes/5
    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @DeleteMapping("/imagenes/{id}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id) {
        imagenService.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }
}