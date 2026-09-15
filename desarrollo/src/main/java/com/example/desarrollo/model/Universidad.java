package com.example.desarrollo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Universidad {
    @Id
    private String nombre;

    private String direccion;

    @OneToMany(mappedBy = "universidad")
    private List<Estudiante> estudiantes =  new ArrayList<>();

    public Universidad(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

}
