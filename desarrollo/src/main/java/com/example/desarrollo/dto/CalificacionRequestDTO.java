package com.example.desarrollo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalificacionRequestDTO {
    @Min(1)
    @Max(5)
    private int puntuacion;

    @Size(max = 500)
    private String descripcion;

    @NotNull
    private Long autorId;

    @NotNull
    private Long receptorId;

    @NotNull
    private Long reservaId;
}
