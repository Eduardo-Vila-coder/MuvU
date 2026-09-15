package com.example.desarrollo.repository;

import com.example.desarrollo.model.Imagen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
}
