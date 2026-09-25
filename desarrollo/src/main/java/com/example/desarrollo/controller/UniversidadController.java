package com.example.desarrollo.controller;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.service.UniversidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/universidades")
@RequiredArgsConstructor
public class UniversidadController {

    private final UniversidadService universidadService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UniversidadResponseDTO> createUniversidad(
            @Valid @RequestBody UniversidadRequestDTO uniRequestDTO) {
        UniversidadResponseDTO uniResponseDTO = universidadService.createUniversidad(uniRequestDTO);
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUni(@PathVariable Long id) {
        universidadService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}