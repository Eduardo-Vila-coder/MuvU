package com.example.desarrollo.service;

import com.example.desarrollo.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    private static final String TIPO_ACCESS = "access";
    private static final String TIPO_REFRESH = "refresh";
    private static final String TIPO_RESET = "reset";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    @Value("${jwt.reset-expiration}")
    private Long resetExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Usuario user) {
        return buildToken(user, TIPO_ACCESS, expiration);
    }

    public String generateRefreshToken(Usuario user) {
        return buildToken(user, TIPO_REFRESH, refreshExpiration);
    }

    // Solo acepta access tokens: un refresh token no sirve para llamar a la API
    public boolean isAccessTokenValid(String token) {
        return tieneTipo(token, TIPO_ACCESS);
    }

    public boolean isRefreshTokenValid(String token) {
        return tieneTipo(token, TIPO_REFRESH);
    }

    // Token corto que se envía por correo para restablecer la contraseña
    public String generateResetToken(Usuario user) {
        return buildToken(user, TIPO_RESET, resetExpiration);
    }

    public boolean isResetTokenValid(String token) {
        return tieneTipo(token, TIPO_RESET);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    private String buildToken(Usuario user, String tipo, long duracion) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .claim("type", tipo)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + duracion))
                .signWith(getSigningKey())
                .compact();
    }

    // Firma inválida, token mal formado o expirado -> false
    private boolean tieneTipo(String token, String tipo) {
        try {
            return tipo.equals(extractAllClaims(token).get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
}
