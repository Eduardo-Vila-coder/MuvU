package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionResponseDTO {
    private Long id;
    private Double precio;
    private String direccion;
    private Integer area;
    private ArrendadorResponseDTO arrendador;
}
