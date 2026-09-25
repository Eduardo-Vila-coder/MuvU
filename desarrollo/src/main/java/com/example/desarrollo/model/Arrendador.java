package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "arrendador")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate   // solo actualiza las columnas que cambian (dos eventos pueden modificarlo a la vez)
public class Arrendador extends Usuario {

    @Size(max = 500)
    @Column(name = "dni_foto", length = 500)
    private String dniFoto;

    @OneToMany(mappedBy = "arrendador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones = new ArrayList<>();

    @Column(name = "puntaje_promedio")
    private Double puntajePromedio = 0.0;

    @Column(name = "total_calificaciones")
    private Long totalCalificaciones = 0L;

    @Column(name = "cantidad_habitaciones")
    private Long cantidadHabitaciones = 0L;
}