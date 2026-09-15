package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final LocalDate fecha_inicio;
    private final LocalDate fecha_fin;
    private Estado estado; // podria ser asi
    ///@ManyToOne
    ///@JoinColumn(name="habitacion_id",nullable=false)
    ///private Habitacion habitacion;
    ///@ManyToOne
    ///@JoinColumn(name="estudiante_id",nullable=false)
    ///private Estudiante estudiante;
    public Reserva(LocalDate fecha_fin){
        fecha_inicio=LocalDate.now();
        this.fecha_fin=fecha_fin;
        estado=Estado.PENDIENTE;
    }
}
