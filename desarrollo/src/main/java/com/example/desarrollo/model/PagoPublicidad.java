package com.example.desarrollo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@Entity
public class PagoPublicidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Habitacion habitacion;

    private Double monto;

    @Column(nullable = false)
    private String metodoDePago;

    private Date fecha_inicio;
    private Date fecha_fin;


}
