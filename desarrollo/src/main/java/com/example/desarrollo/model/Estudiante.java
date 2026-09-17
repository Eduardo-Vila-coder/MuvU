package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String correo;
    private String contrasena;
    private boolean verificado = false;

    @ManyToOne
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas = new ArrayList<>();


    @OneToMany(mappedBy="autor")
    private List<Calificacion> calificacionesDadas = new ArrayList<>();
}
