package com.example.desarrollo.Events;

import com.example.desarrollo.service.ArrendadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ActualizacionPromedioListener {

    private final ArrendadorService service;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void processActualizacionPromedio(ActualizacionPromedioEvent event) {
        service.actualizarPromedio(event.getArrendadorId());
    }
}
