package com.example.desarrollo.repository;

import com.example.desarrollo.model.Estado;
import com.example.desarrollo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva,Long> {
    List<Reserva> findByEstudianteId (Long estudianteId);
    List<Reserva> findByHabitacionArrendadorId (Long arrendadorId);
    List<Reserva> findByHabitacionIdAndEstadoIn(Long habitacionId, Collection<Estado> estados);
}
