package com.example.desarrollo.Events;

import com.example.desarrollo.model.Mail;
import com.example.desarrollo.service.ArrendadorService;
import com.example.desarrollo.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventListenersTest {

    @Mock private ArrendadorService arrendadorService;
    @Mock private EmailService emailService;

    @Test
    void actualizacionPromedio_recalculaElPromedioDelArrendador() {
        new ActualizacionPromedioListener(arrendadorService)
                .processActualizacionPromedio(new ActualizacionPromedioEvent(this, 2L));

        verify(arrendadorService).actualizarPromedio(2L);
    }

    @Test
    void actualizacionHabitaciones_recalculaLaCantidad() {
        new ActualizacionHabitacionesListener(arrendadorService)
                .processActualizacionHabitaciones(new ActualizacionHabitacionesEvent(this, 2L));

        verify(arrendadorService).actualizarCantidadHabitaciones(2L);
    }

    @Test
    void notificacionCorreo_enviaElMail() {
        Mail mail = Mail.para("ana@utec.edu.pe", "Asunto", "Cuerpo");

        new NotificacionCorreoListener(emailService).enviarCorreo(new NotificacionCorreoEvent(this, mail));

        verify(emailService).sendEmailWithThymeLeaf(mail);
    }
}
