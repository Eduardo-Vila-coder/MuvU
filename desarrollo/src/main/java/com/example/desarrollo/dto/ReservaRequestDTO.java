package com.example.desarrollo.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser pasada")
    private LocalDate fecha_inicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser posterior a hoy")
    private LocalDate fecha_fin;

    @NotNull(message = "El ID de la habitación es obligatorio")
    @Positive(message = "El ID de la habitación debe ser positivo")
    private Long habitacionId;
}