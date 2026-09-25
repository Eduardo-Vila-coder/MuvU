package com.example.desarrollo.controller;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.service.UniversidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/universidad")
@RequiredArgsConstructor
public class UniversidadController {

    private final UniversidadService universidadService;

    @PostMapping
    public ResponseEntity<UniversidadResponseDTO> createUniversidad(
            @Valid @RequestBody UniversidadRequestDTO uniRequestDTO) {
        UniversidadResponseDTO uniResponseDTO = universidadService.createUniversidad(uniRequestDTO);

        // Construcción segura del URI usando la ubicación actual y el ID
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(uniResponseDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(uniResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UniversidadResponseDTO>> getAllUnis() {
        List<UniversidadResponseDTO> unis = universidadService.findAllDTO();
        return ResponseEntity.ok(unis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversidadResponseDTO> getUniById(@PathVariable Long id) {
        return ResponseEntity.ok(universidadService.findByIdDTO(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUni(@PathVariable Long id) {
        universidadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}