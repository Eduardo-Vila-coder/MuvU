package com.example.desarrollo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "arrendador")
@Getter
@Setter
@NoArgsConstructor
public class Arrendador extends Usuario {

    @Size(max = 500)
    @Column(name = "dni_foto", length = 500)
    private String dniFoto;

    @OneToMany(mappedBy = "arrendador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Habitacion> habitaciones = new ArrayList<>();
}