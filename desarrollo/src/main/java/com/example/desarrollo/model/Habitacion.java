package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "arrendador_id", nullable = false)
    private Arrendador arrendador;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<Imagen> imagenes;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<PagoPublicidad> pagoPublicidades;
}
