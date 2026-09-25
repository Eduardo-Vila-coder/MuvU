package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImagenRequestDTO {

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 500, message = "La URL no puede superar 500 caracteres")
    @Pattern(regexp = "^https?://\\S+$", message = "La URL debe empezar con http:// o https://")
    private String url;
}