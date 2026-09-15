package com.example.desarrollo.config;

import com.example.desarrollo.dto.CalificacionRequestDTO;
import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.model.Calificacion;
import com.example.desarrollo.model.Estudiante;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
