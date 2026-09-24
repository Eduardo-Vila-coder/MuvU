package com.example.desarrollo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArrendadorRequestDTO {
    @NotBlank //@NotNull // en este caso es para validar cadenas
    private String nombre;

    @NotBlank //@NotNull // lo mismo que antes
    @Email // validacion de email
    private String correo;

    @NotBlank //@NotNull // lo mismo que antes
    @Size(min=8) // constraseña minima de 8
    private String contrasena;
}
