package com.example.desarrollo.Events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ActualizacionPromedioEvent extends ApplicationEvent {

    private final Long arrendadorId;

    public ActualizacionPromedioEvent(Object source, Long arrendadorId) {
        super(source);
        this.arrendadorId = arrendadorId;
    }
}
