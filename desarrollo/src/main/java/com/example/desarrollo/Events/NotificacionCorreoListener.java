package com.example.desarrollo.Events;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionCorreoListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void enviarCorreo(NotificacionCorreoEvent event) {
        log.debug("Evento recibido: enviar correo '{}'", event.getMail().getSubject());
        emailService.sendEmailWithThymeLeaf(event.getMail());
    }
}
