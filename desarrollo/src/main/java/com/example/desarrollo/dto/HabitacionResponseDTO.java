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

    @NotNull
    private String direccion;

    @NotNull
    private Integer area;
}
