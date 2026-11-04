package com.example.desarrollo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Mail {
    private String[] to;
    private String subject;
    private String body;
    private String actionUrl;   // opcional: enlace del botón
    private String actionText;  // opcional: texto del botón

    // Se mantiene para no romper EmailServiceImplTest
    public Mail(String[] to, String subject, String body) {
        this(to, subject, body, null, null);
    }

    public static Mail para(String correo, String asunto, String mensaje) {
        return new Mail(new String[]{correo}, asunto, mensaje);
    }

    public static Mail conAccion(String correo, String asunto, String mensaje,
                                 String actionUrl, String actionText) {
        return new Mail(new String[]{correo}, asunto, mensaje, actionUrl, actionText);
    }
}