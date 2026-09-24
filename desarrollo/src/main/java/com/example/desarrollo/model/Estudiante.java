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

    @Email
    @Column(unique = true) private String correo;
    private String contrasena;
    private boolean verificado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy="autor", fetch = FetchType.EAGER)
    private List<Calificacion> calificacionesDadas = new ArrayList<>();     // modificar
}
