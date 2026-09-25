package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagen", indexes = @Index(name = "idx_imagen_habitacion", columnList = "habitacion_id"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String url;
}