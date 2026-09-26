package com.example.desarrollo.service;

import com.example.desarrollo.exceptions.ForbiddenException;
import com.example.desarrollo.model.Admin;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Rol;
import com.example.desarrollo.model.Usuario;
import com.example.desarrollo.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private UsuarioService usuarioService;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void loadUserByUsername_existente_devuelveUsuario() {
        Usuario ana = usuario(new Estudiante(), 3L, Rol.ESTUDIANTE);
        when(usuarioRepository.findByCorreo("ana@utec.edu.pe")).thenReturn(Optional.of(ana));

        assertSame(ana, usuarioService.loadUserByUsername("ana@utec.edu.pe"));
    }

    @Test
    void loadUserByUsername_inexistente_lanzaUsernameNotFound() {
        when(usuarioRepository.findByCorreo("x@utec.edu.pe")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> usuarioService.loadUserByUsername("x@utec.edu.pe"));
    }

    @Test
    void getIdUsuarioActual_leeElUsuarioDelToken() {
        autenticar(usuario(new Estudiante(), 3L, Rol.ESTUDIANTE));

        assertEquals(3L, usuarioService.getIdUsuarioActual());
    }

    @Test
    void validarQueSoyYo_mismoUsuario_noLanza() {
        autenticar(usuario(new Estudiante(), 3L, Rol.ESTUDIANTE));

        assertDoesNotThrow(() -> usuarioService.validarQueSoyYo(3L));
    }

    @Test
    void validarQueSoyYo_otroUsuario_lanzaForbidden() {
        autenticar(usuario(new Estudiante(), 3L, Rol.ESTUDIANTE));

        assertThrows(ForbiddenException.class, () -> usuarioService.validarQueSoyYo(4L));
    }

    @Test
    void validarQueSoyYo_admin_puedeModificarACualquiera() {
        autenticar(usuario(new Admin(), 1L, Rol.ADMIN));

        assertDoesNotThrow(() -> usuarioService.validarQueSoyYo(4L));
    }

    private Usuario usuario(Usuario u, Long id, Rol rol) {
        u.setId(id);
        u.setRol(rol);
        return u;
    }

    private void autenticar(Usuario u) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities()));
    }
}
