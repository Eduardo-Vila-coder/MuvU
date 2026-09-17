package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int puntuacion;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="autor_id", nullable = false)
    private Estudiante autor;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="receptor_id", nullable = false)
    private Habitacion receptor;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

}
