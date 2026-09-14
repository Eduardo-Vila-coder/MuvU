package com.example.desarrollo.controller;

import com.example.desarrollo.dto.HabitacionDetailDTO;
import com.example.desarrollo.dto.HabitacionRequestDTO;
import com.example.desarrollo.dto.HabitacionResponseDTO;
import com.example.desarrollo.service.HabitacionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habitacion")
public class HabitacionController {
    private final HabitacionService habitacionService;

    @Autowired
    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @PostMapping
    public ResponseEntity<HabitacionResponseDTO> createHabitacion(
            @Valid @RequestBody HabitacionRequestDTO habitacionRequestDTO) {
        HabitacionResponseDTO savedHabitacion = habitacionService.save(habitacionRequestDTO);
        return ResponseEntity.ok(savedHabitacion);
    }

    // Para los clientes que quieren ver la habitacion
    @GetMapping("/{id}")
    public ResponseEntity<HabitacionDetailDTO> getHabitacionById(@PathVariable Long id) {
        HabitacionDetailDTO habitacionDetailDTO = habitacionService.findById(id);

        if (habitacionDetailDTO != null) {
            return ResponseEntity.ok(habitacionDetailDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Para los arrendadores que quieran agregar imagenes a su habitacion
    @PatchMapping("/{id}/add-imagen/{imagen_id}")
    public ResponseEntity<HabitacionDetailDTO> addImagen(@PathVariable("id") Long id, @PathVariable("imagen_id") Long imagen_id) {
        HabitacionDetailDTO habitacionDetailDTO = habitacionService.addImagen(id, imagen_id);
        return ResponseEntity.ok(habitacionDetailDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitacion(@PathVariable Long id) {
        habitacionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
