package com.example.desarrollo.dto;

import com.example.desarrollo.model.Universidad;
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
public class EstudianteRequestDTO {
    @NotNull
    private String nombre;

    @NotNull
    @Email
    private String correo;

    @NotNull
    private String contrasena;

    @NotNull
    private Universidad universidad;
}
