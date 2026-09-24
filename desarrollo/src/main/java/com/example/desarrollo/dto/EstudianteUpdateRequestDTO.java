package com.example.desarrollo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank //@NotNull
    private String nombre;

    @NotBlank //@NotNull
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.(edu\\.pe)$")
    private String correo;

    @NotNull
    private Long universidadId;
}
