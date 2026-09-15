package com.example.desarrollo.dto;

import com.example.desarrollo.model.Universidad;
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
public class EstudianteRequestRegisterDTO {
    @NotBlank(message = "El nombre es obligatorio")
    String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es valido")
    String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String contrasena;

    @NotNull(message = "El ID de la universidad es obligatorio")
    UniversidadSimpleDTO universidad;
}
