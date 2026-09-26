package com.example.desarrollo.service;

import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Rol;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UsuarioService usuarioService;

    @InjectMocks private JwtAuthorizationFilter filter;

    private final MockFilterChain chain = new MockFilterChain();

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void sinHeader_noAutenticaPeroContinua() throws Exception {
        filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
        verifyNoInteractions(jwtService);
    }

    @Test
    void tokenValido_autenticaAlUsuario() throws Exception {
        Estudiante ana = new Estudiante();
        ana.setCorreo("ana@utec.edu.pe");
        ana.setRol(Rol.ESTUDIANTE);
        when(jwtService.isAccessTokenValid("abc")).thenReturn(true);
        when(jwtService.extractUsername("abc")).thenReturn("ana@utec.edu.pe");
        when(usuarioService.loadUserByUsername("ana@utec.edu.pe")).thenReturn(ana);

        filter.doFilter(conToken("abc"), new MockHttpServletResponse(), chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertSame(ana, auth.getPrincipal());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> "ESTUDIANTE".equals(a.getAuthority())));
    }

    @Test
    void tokenInvalido_noAutentica() throws Exception {
        when(jwtService.isAccessTokenValid("malo")).thenReturn(false);

        filter.doFilter(conToken("malo"), new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(usuarioService, never()).loadUserByUsername(anyString());
    }

    @Test
    void usuarioEliminado_noAutentica() throws Exception {
        when(jwtService.isAccessTokenValid("abc")).thenReturn(true);
        when(jwtService.extractUsername("abc")).thenReturn("borrado@utec.edu.pe");
        when(usuarioService.loadUserByUsername("borrado@utec.edu.pe")).thenThrow(new UsernameNotFoundException("x"));

        filter.doFilter(conToken("abc"), new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    private MockHttpServletRequest conToken(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
