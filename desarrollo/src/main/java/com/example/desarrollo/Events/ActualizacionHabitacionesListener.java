package com.example.desarrollo.Events;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.service.ArrendadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActualizacionHabitacionesListener {

    private final ArrendadorService service;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void processActualizacionHabitaciones(ActualizacionHabitacionesEvent event) {
        log.debug("Evento recibido: recalcular habitaciones del arrendador {}", event.getArrendadorId());
        service.actualizarCantidadHabitaciones(event.getArrendadorId());
    }
}
