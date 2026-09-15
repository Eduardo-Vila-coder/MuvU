package com.example.desarrollo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalificacionResponseDTO {
    private Long id;
    private int puntuacion;
    private String descripcion;
    private Long autorId;
    private String autorNombre;
    private Long receptorId;
    private String receptorNombre;
    private Long reservaId;
}
