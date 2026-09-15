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

public class EstudianteResponseRegisterDTO {
    Long id;
    String nombre;
    String correo;
    Boolean verificado;
    UniversidadSimpleDTO universidad;
}
