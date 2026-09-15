package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Arrendador extends Usuario {
    private String dniFoto;

    @OneToMany(mappedBy = "arrendador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Arrendador(String nombre, String correo, String contrasena, String dniFoto) {
        super(nombre, correo, contrasena);
        this.dniFoto = dniFoto;
    }
}
