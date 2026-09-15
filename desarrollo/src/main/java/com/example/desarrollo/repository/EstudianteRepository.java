package com.example.desarrollo.repository;

import com.example.desarrollo.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long>  {
    boolean existsByCorreo(String correo);
}
