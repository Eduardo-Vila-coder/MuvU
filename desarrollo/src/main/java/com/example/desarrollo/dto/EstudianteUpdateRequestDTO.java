package com.example.desarrollo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class EstudianteUpdateRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre solo puede contener letras, espacios, puntos, apóstrofes y guiones")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.edu\\.pe$", message = "Debes usar tu correo institucional (.edu.pe)")
    @Size(max = 150, message = "El correo no puede superar 150 caracteres")
    private String correo;

    @NotNull(message = "La universidad es obligatoria")
    @Positive(message = "El ID de la universidad debe ser positivo")
    private Long universidadId;
}