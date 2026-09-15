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
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"reserva_id","autor_id"}))
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int puntuacion;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name="autor_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name="receptor_id", nullable = false)
    private Usuario receptor;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    public Calificacion(int puntuacion, String descripcion, Usuario autor, Usuario receptor, Reserva reserva) {
        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.autor = autor;
        this.receptor = receptor;
        this.reserva = reserva;
    }
}
