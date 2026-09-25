package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

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

    @NotBlank
    private String direccion;

    @Column(nullable = true)
    private Double latitud;

    @Column(nullable = true)
    private Double longitud;

    @Positive
    private double precio; // Double

    @Positive
    private Integer area;

    private Boolean esDestacada = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "arrendador_id", nullable = false)
    @Valid
    private Arrendador arrendador;

    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Imagen> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "habitacion", fetch = FetchType.LAZY)
    private List<PagoPublicidad> pagoPublicidades = new ArrayList<>();

    @OneToMany(mappedBy="receptor", fetch = FetchType.EAGER)
    private List<Calificacion> calificacionesRecibidas = new ArrayList<>();

}
