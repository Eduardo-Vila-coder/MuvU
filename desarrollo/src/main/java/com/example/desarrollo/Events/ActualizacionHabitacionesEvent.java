package com.example.desarrollo.Events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ActualizacionHabitacionesEvent extends ApplicationEvent {

    private final Long arrendadorId;

    public ActualizacionHabitacionesEvent(Object source, Long arrendadorId) {
        super(source);
        this.arrendadorId = arrendadorId;
    }
}
