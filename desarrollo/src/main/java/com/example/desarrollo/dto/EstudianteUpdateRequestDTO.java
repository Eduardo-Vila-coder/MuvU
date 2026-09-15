package com.example.desarrollo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteUpdateRequestDTO {
    @NotNull
    private String nombre;

    @NotNull
    @Email
    private String correo;

    @NotNull
    private Long universidadId;
}
