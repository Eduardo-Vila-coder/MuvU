package com.example.desarrollo.controller;

import com.example.desarrollo.dto.PagoPublicidadRequestDTO;
import com.example.desarrollo.dto.PagoPublicidadResponseDTO;
import com.example.desarrollo.service.PagoPublicidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos-publicidad")
@RequiredArgsConstructor
public class PagoPublicidadController {

    private final PagoPublicidadService pagoPublicidadService;


    @PostMapping
    public ResponseEntity<PagoPublicidadResponseDTO> registrarPago(@Valid @RequestBody PagoPublicidadRequestDTO requestDTO) {
        PagoPublicidadResponseDTO nuevoPago = pagoPublicidadService.registrarPago(requestDTO);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<PagoPublicidadResponseDTO>> listarTodos() {
        List<PagoPublicidadResponseDTO> lista = pagoPublicidadService.listarTodos();
        return ResponseEntity.ok(lista);
    }
}
