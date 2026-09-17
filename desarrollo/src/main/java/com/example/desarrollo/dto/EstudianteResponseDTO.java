package com.example.desarrollo.dto;

import com.example.desarrollo.model.Universidad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private boolean verificado;
    private UniversidadResponseDTO Universidad;
}
