package com.example.desarrollo.controller;

import com.example.desarrollo.dto.EstudianteRequestRegisterDTO;
import com.example.desarrollo.dto.EstudianteResponseRegisterDTO;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.service.EstudianteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> findAll() {
        List <Estudiante> estudiantes = estudianteService.findAll();
        return ResponseEntity.ok().body(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> findById(@PathVariable Long id) {
        Estudiante estudiante = estudianteService.findById(id);
        return ResponseEntity.ok().body(estudiante);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        estudianteService.deleteById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<EstudianteResponseRegisterDTO> register(@Valid @RequestBody EstudianteRequestRegisterDTO estudiante) {
        EstudianteResponseRegisterDTO response = estudianteService.registrarEstudiante(estudiante);
        return  ResponseEntity.ok().body(response);
    }
}
