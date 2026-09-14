package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionDetailDTO {
    private Long id;

    @NotNull
    private String direccion;

    @NotNull
    private double precio;

    @NotNull
    private Integer area;
    private List<Imagen> imagenes;
}
