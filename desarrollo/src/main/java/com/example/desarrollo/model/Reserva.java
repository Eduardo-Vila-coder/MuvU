package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;


    public Reserva(LocalDate fecha_fin, Estudiante estudiante, Habitacion habitacion) {
        fecha_inicio=LocalDate.now();
        this.fecha_fin=fecha_fin;
        this.estudiante=estudiante;
        this.habitacion=habitacion;
    }
}
