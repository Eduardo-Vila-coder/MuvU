package com.example.desarrollo.dto;

import com.example.desarrollo.model.MetodoPago;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoPublicidadRequestDTO {
    @NotNull(message = "El ID de la habitación es obligatorio")
    private Long habitacionId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private double monto;

    @NotNull(message = "El método de pago es obligatorio")
    @Enumerated(EnumType.STRING) // para el enum
    private MetodoPago metodoDePago;
}
