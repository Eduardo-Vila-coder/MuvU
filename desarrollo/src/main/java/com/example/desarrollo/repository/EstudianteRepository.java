package com.example.desarrollo.repository;

import com.example.desarrollo.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    @Override
    @EntityGraph(attributePaths = "universidad")
    Page<Estudiante> findAll(Pageable pageable);
}