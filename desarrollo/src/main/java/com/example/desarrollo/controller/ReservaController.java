package com.example.desarrollo.controller;

import com.example.desarrollo.dto.ReservaRequestDTO;
import com.example.desarrollo.dto.ReservaResponseDTO;
import com.example.desarrollo.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> getMisReservas() {
        return ResponseEntity.ok(reservaService.findMisReservas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> getReservaById(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.findById(id));
    }

    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    @PostMapping
    public ResponseEntity<ReservaResponseDTO> createReserva(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        ReservaResponseDTO reservaResponseDTO = reservaService.createReserva(reservaRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservaResponseDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(reservaResponseDTO);
    }

    @PreAuthorize("hasAuthority('ESTUDIANTE')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.cancelReserva(id));
    }

    @PreAuthorize("hasAuthority('ARRENDADOR')")
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<ReservaResponseDTO> confirmReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.confirmReserva(id));
    }
}
