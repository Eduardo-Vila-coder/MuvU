package com.example.desarrollo.dto;

import com.example.desarrollo.model.Estado;
import com.example.desarrollo.service.EstudianteService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaResponseDTO {
    private Long id;
    private Estado estado;
    private LocalDate fecha_fin;
    private LocalDate fecha_inicio;

    private HabitacionIdArrendadorDTO habitacion;
    private EstudianteSimpleDTO estudiante;
}
