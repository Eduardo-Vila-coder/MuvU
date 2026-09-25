package com.example.desarrollo.Events;

import com.example.desarrollo.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificacionCorreoEvent extends ApplicationEvent {

    private final Mail mail;

    public NotificacionCorreoEvent(Object source, Mail mail) {
        super(source);
        this.mail = mail;
    }
}
