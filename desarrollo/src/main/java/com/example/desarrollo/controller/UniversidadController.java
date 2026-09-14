package com.example.desarrollo.controller;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.service.UniversidadService;
import jakarta.validation.Valid;
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
        URI location = URI.create("universidad/"+ uniResponseDTO.getNombre());
        return ResponseEntity.created(location).body(uniResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<Universidad>> getAllUnis() {
        List<Universidad> unis = universidadService.findAll();
        return ResponseEntity.ok(unis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Universidad> getUniById(@PathVariable String name) {
        Universidad uni = universidadService.findById(name);
        if (uni!=null){
            return ResponseEntity.ok(uni);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUni(@PathVariable String name) {
        universidadService.deleteById(name);
        return ResponseEntity.noContent().build();//o ok() en vez de .noContent()
    }
}
