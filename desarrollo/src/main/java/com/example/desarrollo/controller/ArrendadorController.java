package com.example.desarrollo.controller;

import com.example.desarrollo.dto.ArrendadorRequestDTO;
import com.example.desarrollo.dto.ArrendadorResponseDTO;
import com.example.desarrollo.dto.ArrendadorUpdateRequestDTO;
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


    @GetMapping("/{id}")
    public ResponseEntity<ArrendadorResponseDTO> getArrendadorById(@PathVariable Long id) {
        return ResponseEntity.ok(arrendadorService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArrendadorResponseDTO> updateArrendador(
            @PathVariable Long id, @Valid @RequestBody ArrendadorUpdateRequestDTO dto) {
        return ResponseEntity.ok(arrendadorService.update(id, dto));
    }

    @PatchMapping("/{id}/verificar")
    public ResponseEntity<ArrendadorResponseDTO> verificarArrendador(@PathVariable Long id) {
        return ResponseEntity.ok(arrendadorService.verificar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArrendador(@PathVariable Long id) {
        arrendadorService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
