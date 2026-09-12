package com.example.desarrollo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ImagenRequestDTO {

    //vas a requerir el ID; sin embargo, esta estará como un parametro...
    //no deberia importar si es querry o path param no?

    @NotNull(message = "La URL o ruta de la imagen es obligatoria")
    private String url;
}
