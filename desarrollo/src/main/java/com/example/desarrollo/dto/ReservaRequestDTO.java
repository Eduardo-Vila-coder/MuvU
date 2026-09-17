package com.example.desarrollo.dto;

import com.example.desarrollo.model.Estado;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDTO {
    @NotNull private LocalDate fecha_fin;
    private EstudianteConsultaDTO estudiante;
    private HabitacionConsultaDTO habitacion;
}
