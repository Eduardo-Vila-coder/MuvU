package com.example.desarrollo.repository;

import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva,Long> {
    Page<Reserva> findByEstudianteId(Long estudianteId, Pageable pageable);
    Page<Reserva> findByHabitacionArrendadorId(Long arrendadorId, Pageable pageable);
    List<Reserva> findByHabitacionIdAndEstadoIn(Long habitacionId, Collection<Estado> estados);
}
