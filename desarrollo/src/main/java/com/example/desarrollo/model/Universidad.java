package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "universidad")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Universidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String direccion;

    private Double latitud;

    private Double longitud;

    @OneToMany(mappedBy = "universidad", fetch = FetchType.LAZY)
    private List<Estudiante> estudiantes = new ArrayList<>();
}