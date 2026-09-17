package com.example.desarrollo.controller;

import com.example.desarrollo.service.CalificacionService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

}