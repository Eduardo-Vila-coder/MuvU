package com.example.desarrollo.repository;

import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
