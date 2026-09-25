package com.example.desarrollo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull()
    @Min(value = 1)
    @Max(value = 5)
    private Integer puntuacion;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    private Long reservaId;

}
