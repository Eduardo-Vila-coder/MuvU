package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PagoPublicidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "habitacion_id")
//    private Habitacion habitacion;

    private Double monto;

    @Column(nullable = false)
    private String metodoDePago;

    private Date fechaInicio;
    private Date fechaFin;


}
