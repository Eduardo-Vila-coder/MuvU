package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UniversidadRequestDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String direccion;
}
