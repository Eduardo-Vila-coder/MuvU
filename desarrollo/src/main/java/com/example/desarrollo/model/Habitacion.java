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
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private Double longitud;
    private Double latitud;
    private double precio;
    private Integer area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrendador_id", nullable = false)
    private Arrendador arrendador;

    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<PagoPublicidad> pagoPublicidades = new ArrayList<>();

    @OneToMany(mappedBy="receptor")
    private List<Calificacion> calificacionesRecibidas = new ArrayList<>();

}
