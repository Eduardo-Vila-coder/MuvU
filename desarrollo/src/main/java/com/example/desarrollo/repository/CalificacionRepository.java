package com.example.desarrollo.repository;

import com.example.desarrollo.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    @Query("select avg(c.puntuacion) from Calificacion c where c.receptor.id = :usuarioId")
    Double findPromedioByReceptorId(@Param("usuarioId") Long usuarioId);

    long countByReceptorId(Long receptorId);
    boolean existsByReservaIdAndAutorId(Long reservaId, Long autorId);

    List<Calificacion> findByReceptorId(Long receptorId);
    List<Calificacion> findByAutorId(Long autorId);
}
