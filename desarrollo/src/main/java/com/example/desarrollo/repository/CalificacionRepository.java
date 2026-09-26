package com.example.desarrollo.repository;

import com.example.desarrollo.model.Calificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    @Query("select avg(c.puntuacion) from Calificacion c where c.receptor.id = :receptorId")
    Double findPromedioByReceptorId(@Param("receptorId") Long receptorId);

    long countByReceptorId(Long receptorId);

    @Query("select avg(c.puntuacion) from Calificacion c where c.autor.id = :autorId")
    Double findPromedioByAutorId(@Param("autorId") Long autorId);

    long countByAutorId(Long autorId);

    boolean existsByReservaIdAndAutorId(Long reservaId, Long autorId);

    @EntityGraph(attributePaths = "autor")
    Page<Calificacion> findByReceptorId(Long receptorId, Pageable pageable);

    Page<Calificacion> findByAutorId(Long autorId, Pageable pageable);

    List<Calificacion> findByReceptorArrendadorId(Long arrendadorId);
}