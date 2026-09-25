package com.example.desarrollo.controller;

import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.service.PagoPublicidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos-publicidad")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ARRENDADOR')")   // todo el controller es solo para arrendadores
public class PagoPublicidadController {

    private final PagoPublicidadService pagoPublicidadService;


    @PostMapping
    public ResponseEntity<PagoPublicidadResponseDTO> registrarPago(@Valid @RequestBody PagoPublicidadRequestDTO requestDTO) {
        PagoPublicidadResponseDTO nuevoPago = pagoPublicidadService.registrarPago(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }


    @GetMapping
    public ResponseEntity<List<PagoPublicidadResponseDTO>> listarTodos() {
        List<PagoPublicidadResponseDTO> lista = pagoPublicidadService.listarTodos();
        return ResponseEntity.ok(lista);
    }
}
