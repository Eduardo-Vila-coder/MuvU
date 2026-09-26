package com.example.desarrollo.service;

import com.example.desarrollo.model.Mail;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock private JavaMailSender mailSender;
    @Mock private TemplateEngine templateEngine;

    @InjectMocks private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "remitente", "muvu.notificaciones@gmail.com");
    }

    @Test
    void enviaUnCorreoPorDestinatarioConLaPlantilla() throws Exception {
        when(templateEngine.process(eq("ThymeLeafMail"), any(Context.class))).thenReturn("<p>Hola</p>");
        when(mailSender.createMimeMessage()).thenAnswer(inv -> new MimeMessage((Session) null));

        emailService.sendEmailWithThymeLeaf(
                new Mail(new String[]{"ana@utec.edu.pe", "rosa@muvu.com"}, "MuvU: prueba", "Hola"));

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender, times(2)).send(captor.capture());
        assertEquals("MuvU: prueba", captor.getAllValues().get(0).getSubject());
        assertEquals("ana@utec.edu.pe", captor.getAllValues().get(0).getAllRecipients()[0].toString());
        assertEquals("rosa@muvu.com", captor.getAllValues().get(1).getAllRecipients()[0].toString());
    }

    @Test
    void siUnEnvioFalla_noLanzaYSigueConLosDemas() {
        when(templateEngine.process(eq("ThymeLeafMail"), any(Context.class))).thenReturn("<p>Hola</p>");
        when(mailSender.createMimeMessage()).thenAnswer(inv -> new MimeMessage((Session) null));
        doThrow(new MailSendException("SMTP caído")).doNothing().when(mailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailService.sendEmailWithThymeLeaf(
                new Mail(new String[]{"ana@utec.edu.pe", "rosa@muvu.com"}, "MuvU: prueba", "Hola")));
        verify(mailSender, times(2)).send(any(MimeMessage.class));
    }
}
