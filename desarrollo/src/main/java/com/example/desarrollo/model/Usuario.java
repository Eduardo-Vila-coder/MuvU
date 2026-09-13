package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(unique = true)
    private String correo;
    private String contrasena;
    private boolean verificado = false;
    @OneToMany(mappedBy="autor")
    private List<Calificacion> calificacionesDadas = new ArrayList<>();
    @OneToMany(mappedBy="receptor")
    private List<Calificacion> calificacionesRecibidas = new ArrayList<>();
    private String nombre;

    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }
}
