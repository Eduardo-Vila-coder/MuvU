package com.example.desarrollo.dto;

import com.example.desarrollo.model.Arrendador;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HabitacionRequestDTO {
    @NotBlank //@NotNull // en este caso el validador es para string
    private String direccion;

    @NotNull
    @Positive // para que sea positivo
    private double precio;

    @NotNull
    @Positive // para que el area sea positiva
    private Integer area;

    @NotNull
    @Valid // para validar el arrendador
    private Arrendador arrendador;
}
