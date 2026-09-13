package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
public class Reserva {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private final LocalDate fecha_inicio;
    @Getter
    private final LocalDate fecha_fin;
    @Getter
    @Setter
    private Estado estado; // podria ser asi
    ///@ManyToOne
    ///@Column(name="habitacion_id",nullable=false)
    ///private Habitacion habitacion;
    public Reserva(LocalDate fecha_fin){
        fecha_inicio=LocalDate.now();
        this.fecha_fin=fecha_fin;
        estado=Estado.PENDIENTE;
    }
}
