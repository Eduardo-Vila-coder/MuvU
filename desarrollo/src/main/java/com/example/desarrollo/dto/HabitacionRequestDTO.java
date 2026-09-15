package com.example.desarrollo.dto;

import com.example.desarrollo.model.Arrendador;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionRequestDTO {
    @NotNull
    private String direccion;

    private double precio;

    @NotNull
    private Integer area;

    @NotNull
    private Arrendador arrendador;
}
