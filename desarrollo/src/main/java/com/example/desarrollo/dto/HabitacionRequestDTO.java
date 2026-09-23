package com.example.desarrollo.dto;

import com.example.desarrollo.model.Arrendador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionRequestDTO {
    @NotBlank
    private String direccion;

    @NotNull
    @Positive
    private double precio;

    @NotNull
    @Positive
    private Integer area;

    @NotNull
    private Arrendador arrendador; // Cambiar por el ID del arrendador
}
