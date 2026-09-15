package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private double precio;
    private Integer area;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "arrendador_id", nullable = false)
    private Arrendador arrendador;

    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<PagoPublicidad> pagoPublicidades = new ArrayList<>();

    public Habitacion(String direccion, double precio, Integer area, Arrendador arrendador) {
        this.direccion = direccion;
        this.precio = precio;
        this.area = area;
        this.arrendador = arrendador;
    }
}
