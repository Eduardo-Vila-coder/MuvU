package com.example.desarrollo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Universidad {

    @Id
    private String nombre;

    //la otra opcion que me recomienda la IA:
    // public record direccion(String calle, String ciudad, String codigoPostal, String pais) {}

    private String direccion;
}
