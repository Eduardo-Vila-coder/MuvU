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
    private String [] to;
    private String subject;
    private String body;

    public static Mail para(String correo, String asunto, String mensaje) {
        return new Mail(new String[]{correo}, asunto, mensaje);
    }
}
