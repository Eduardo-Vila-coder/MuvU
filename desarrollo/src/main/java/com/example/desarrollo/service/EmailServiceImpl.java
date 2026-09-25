package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.exceptions.ExternalServiceException;
import com.example.desarrollo.model.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remitente;

    @Override
    public void sendEmailWithThymeLeaf(Mail mail) {
        Context context = new Context();
        context.setVariable("body", mail.getBody());
        String html = templateEngine.process("ThymeLeafMail", context);

        for (String destinatario : mail.getTo()) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
                helper.setFrom(remitente);
                helper.setTo(destinatario);
                helper.setSubject(mail.getSubject());
                helper.setText(html, true);
                mailSender.send(message);
                log.info("Correo '{}' enviado a {}", mail.getSubject(), destinatario);
            } catch (MessagingException e) {
                log.error("No se pudo armar el correo para {}", destinatario, e);
                throw new ExternalServiceException("No se pudo enviar el correo a " + destinatario, e);
            }
        }
    }
}
