package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionRequestDTO {

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String direccion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @NotNull(message = "El área es obligatoria")
    @Positive(message = "El área debe ser mayor a cero")
    private Integer area;
}