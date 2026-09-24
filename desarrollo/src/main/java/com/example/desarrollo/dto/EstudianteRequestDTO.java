package com.example.desarrollo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteRequestDTO {
    @NotBlank //@NotNull para validar cadenas
    private String nombre;

    @NotBlank //@NotNull
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.edu\\.pe$") // en teoria deberia funcionar
    private String correo;

    @NotBlank // @NotNull
    @Size(min=8) // cadena minima de 8
    private String contrasena;

    @NotNull
    private Long universidadId;
}
