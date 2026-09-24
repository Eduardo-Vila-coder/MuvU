package com.example.desarrollo.config;

import com.example.desarrollo.service.JwtAuthorizationFilter;
import com.example.desarrollo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtAuthorizationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())   // para que funcione tu @CrossOrigin
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Públicos
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/habitacion/**", "/universidad/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/calificaciones/habitacion/**").permitAll()

                        // Solo ARRENDADOR
                        .requestMatchers(HttpMethod.POST, "/habitacion").hasAuthority("ARRENDADOR")
                        .requestMatchers(HttpMethod.PATCH, "/habitacion/**").hasAuthority("ARRENDADOR")
                        .requestMatchers(HttpMethod.DELETE, "/habitacion/**").hasAuthority("ARRENDADOR")
                        .requestMatchers("/api/pagos-publicidad/**").hasAuthority("ARRENDADOR")
                        .requestMatchers(HttpMethod.GET, "/estudiantes/*/perfil").hasAuthority("ARRENDADOR")

                        // Solo ESTUDIANTE
                        .requestMatchers(HttpMethod.POST, "/reservas").hasAuthority("ESTUDIANTE")
                        .requestMatchers(HttpMethod.PATCH, "/reservas/**").hasAuthority("ESTUDIANTE")
                        .requestMatchers(HttpMethod.POST, "/calificaciones").hasAuthority("ESTUDIANTE")

                        // Lo demas lo puede hacer cualquier usuario logueado
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}