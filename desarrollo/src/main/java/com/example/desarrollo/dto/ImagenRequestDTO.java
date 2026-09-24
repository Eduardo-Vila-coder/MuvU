package com.example.desarrollo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ImagenRequestDTO {

    //Vamos a requerir el ID; sin embargo, esta estará como un parametro...
    //¿El tipo del parametro no debería importar si es query o path param no?

    @NotBlank(message = "La URL o ruta de la imagen es obligatoria")//@NotNull(message = "La URL o ruta de la imagen es obligatoria")
    private String url;
}
