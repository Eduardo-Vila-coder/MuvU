package com.example.desarrollo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalificacionRequestDTO {

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser positivo")
    private Long reservaId;
}