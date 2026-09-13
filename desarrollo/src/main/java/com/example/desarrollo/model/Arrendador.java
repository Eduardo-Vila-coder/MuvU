package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Arrendador extends Usuario {
    private String dni_foto;

    @OneToMany(mappedBy = "arrendador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones;


    // Los 2 ManyToMany estarán en periodo de revisión
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "arrendador_calificacionEstudiante",
            joinColumns = @JoinColumn(name = "arrendador_id"),
            inverseJoinColumns = @JoinColumn(name = "calificacionEstudiante_id") // Agregarle ID a las relaciones
    )
    private List<CalificacionEstudiante> calificacionEstudiantes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "arrendador_calificacionArrendador",
            joinColumns = @JoinColumn(name = "arrendador_id"),
            inverseJoinColumns = @JoinColumn(name = "calificaionArrendador_id")
    )
    private List<CalificacionArrendador> calificacionArrendadores;

    public Arrendador(boolean verificado, String correo, String nombre, String contrasena, String dni_foto) {
        super(verificado, correo, nombre, contrasena);
        this.dni_foto = dni_foto;
    }
}
