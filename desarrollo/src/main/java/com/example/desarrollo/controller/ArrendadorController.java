package com.example.desarrollo.controller;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.ArrendadorResponseDTO;
import com.example.desarrollo.service.ArrendadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arrendador")
public class ArrendadorController {
    private final ArrendadorService arrendadorService;

    @Autowired
    public ArrendadorController(ArrendadorService arrendadorService) {
        this.arrendadorService = arrendadorService;
    }

    @PostMapping
    public ResponseEntity<ArrendadorResponseDTO> createArrendador(
            @Valid @RequestBody ArrendadorRequestDTO arrendadorRequestDTO) {
        ArrendadorResponseDTO savedArrendador = arrendadorService.guardar(arrendadorRequestDTO);
        return ResponseEntity.ok(savedArrendador);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArrendadorResponseDTO> getArrendadorById(@PathVariable Long id) {
        ArrendadorResponseDTO arrendadorResponseDTO = arrendadorService.findByIdDTO(id);

        if (arrendadorResponseDTO != null) {
            return ResponseEntity.ok(arrendadorResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArrendador(@PathVariable Long id) {
        arrendadorService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
