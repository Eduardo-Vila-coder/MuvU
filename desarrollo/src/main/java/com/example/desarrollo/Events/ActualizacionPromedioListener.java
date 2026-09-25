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
public class ActualizacionPromedioListener {

    private final ArrendadorService service;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void processActualizacionPromedio(ActualizacionPromedioEvent event) {
        log.debug("Evento recibido: recalcular promedio del arrendador {}", event.getArrendadorId());
        service.actualizarPromedio(event.getArrendadorId());
    }
}
