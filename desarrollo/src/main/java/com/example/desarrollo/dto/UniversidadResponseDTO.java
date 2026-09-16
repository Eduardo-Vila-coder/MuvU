package com.example.desarrollo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversidadResponseDTO {
    private Long id;
    private String nombre;
    private String direccion;
}
