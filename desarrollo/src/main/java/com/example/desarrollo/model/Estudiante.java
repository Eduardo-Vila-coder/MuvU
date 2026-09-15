package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="usuario_id")
public class Estudiante extends Usuario{

    @ManyToOne
    private Universidad universidad;

    @OneToMany(mappedBy = "estudiante")
    private List<Reserva> reservas = new ArrayList<>();

    public Estudiante(String nombre, String correo, String contrasena, Universidad universidad) {
        super(nombre, correo, contrasena);
        this.universidad = universidad;
    }
}
