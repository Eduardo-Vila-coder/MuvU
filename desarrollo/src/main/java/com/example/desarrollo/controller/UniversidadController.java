package com.example.desarrollo.controller;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.service.UniversidadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/universidad")
public class UniversidadController {

    private final UniversidadService universidadService;

    public UniversidadController(UniversidadService universidadService) {
        this.universidadService = universidadService;
    }

    @PostMapping
    public ResponseEntity<UniversidadResponseDTO> createUniversidad(@Valid @RequestBody UniversidadRequestDTO uniRequestDTO) {
        UniversidadResponseDTO uniResponseDTO = universidadService.createUniversidad(uniRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(uniResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UniversidadResponseDTO>> getAllUnis() {
        List<UniversidadResponseDTO> universidades = universidadService.findAllDTO();

        if (universidades.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 No Content si la lista está vacía
        }

        return ResponseEntity.ok(universidades); // HTTP 200 OK con el listado de DTOs
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversidadResponseDTO> getUniById(@PathVariable Long id) {
        UniversidadResponseDTO uni = universidadService.findByIdDTO(id);
        if (uni == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(uni);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUni(@PathVariable Long id) {
        universidadService.deleteById(id);
        return ResponseEntity.noContent().build();//o ok() en vez de .noContent()
    }
}
