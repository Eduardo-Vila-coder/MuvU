package com.example.desarrollo.service;

import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = configurar(new JwtService(), "clave-secreta-de-prueba-muvu-con-mas-de-32-caracteres");
    private Estudiante ana;

    @BeforeEach
    void setUp() {
        ana = new Estudiante();
        ana.setId(3L);
        ana.setCorreo("ana@utec.edu.pe");
        ana.setRol(Rol.ESTUDIANTE);
    }

    @Test
    void accessToken_esValidoYContieneCorreoEId() {
        String token = jwtService.generateToken(ana);

        assertTrue(jwtService.isAccessTokenValid(token));
        assertEquals("ana@utec.edu.pe", jwtService.extractUsername(token));
        assertEquals(3L, jwtService.extractUserId(token));
    }

    @Test
    void cadaTipoDeToken_soloSirveParaLoSuyo() {
        String refresh = jwtService.generateRefreshToken(ana);
        String reset = jwtService.generateResetToken(ana);

        assertTrue(jwtService.isRefreshTokenValid(refresh));
        assertFalse(jwtService.isAccessTokenValid(refresh));
        assertTrue(jwtService.isResetTokenValid(reset));
        assertFalse(jwtService.isAccessTokenValid(reset));
        assertFalse(jwtService.isResetTokenValid(jwtService.generateToken(ana)));
    }

    @Test
    void tokenAlterado_noEsValido() {
        Estudiante otro = new Estudiante();
        otro.setId(4L);
        otro.setCorreo("otro@utec.edu.pe");
        otro.setRol(Rol.ADMIN);
        String[] original = jwtService.generateToken(ana).split("\\.");
        String[] ajeno = jwtService.generateToken(otro).split("\\.");
        String alterado = original[0] + "." + ajeno[1] + "." + original[2];

        assertFalse(jwtService.isAccessTokenValid(alterado));
        assertFalse(jwtService.isAccessTokenValid("no-es-un-jwt"));
    }

    @Test
    void tokenExpirado_noEsValido() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1_000L);

        assertFalse(jwtService.isAccessTokenValid(jwtService.generateToken(ana)));
    }

    @Test
    void tokenFirmadoConOtraClave_noEsValido() {
        JwtService otro = configurar(new JwtService(), "otra-clave-secreta-distinta-tambien-de-mas-de-32");

        assertFalse(jwtService.isAccessTokenValid(otro.generateToken(ana)));
    }

    private static JwtService configurar(JwtService service, String secreto) {
        ReflectionTestUtils.setField(service, "secret", secreto);
        ReflectionTestUtils.setField(service, "expiration", 3_600_000L);
        ReflectionTestUtils.setField(service, "refreshExpiration", 604_800_000L);
        ReflectionTestUtils.setField(service, "resetExpiration", 900_000L);
        return service;
    }
}
