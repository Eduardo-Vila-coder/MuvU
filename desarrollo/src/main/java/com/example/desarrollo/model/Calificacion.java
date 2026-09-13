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
    @ManyToOne
    @JoinColumn(name="autor_id")
    private Usuario autor;
    @ManyToOne
    @JoinColumn(name="receptor_id")
    private Usuario receptor;
    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;
    public Calificacion(int puntuacion, String descripcion, Usuario autor, Usuario receptor, Reserva reserva) {
        this.puntuacion = puntuacion;
        this.descripcion = descripcion;
        this.autor = autor;
        this.receptor = receptor;
        this.reserva = reserva;
    }
}
