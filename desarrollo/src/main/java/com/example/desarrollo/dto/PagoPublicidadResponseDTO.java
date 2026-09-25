package com.example.desarrollo.dto;

import com.example.desarrollo.model.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoPublicidadResponseDTO {
    private Long id;
    private Long habitacionId;
    private Double monto;
    private MetodoPago metodoPago;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
