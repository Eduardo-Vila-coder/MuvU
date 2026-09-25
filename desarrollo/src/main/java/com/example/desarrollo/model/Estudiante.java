package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiante", indexes = @Index(name = "idx_estudiante_universidad", columnList = "universidad_id"))
@Getter
@Setter
@NoArgsConstructor
public class Estudiante extends Usuario {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "universidad_id", nullable = false)
    private Universidad universidad;

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Calificacion> calificacionesDadas = new ArrayList<>();
}