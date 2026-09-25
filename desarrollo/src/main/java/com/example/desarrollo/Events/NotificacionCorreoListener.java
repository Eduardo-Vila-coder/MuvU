package com.example.desarrollo.Events;

import com.example.desarrollo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificacionCorreoListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void enviarCorreo(NotificacionCorreoEvent event) {
        emailService.sendEmailWithThymeLeaf(event.getMail());
    }
}
