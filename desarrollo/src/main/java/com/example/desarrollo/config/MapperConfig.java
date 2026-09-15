package com.example.desarrollo.config;

import com.example.desarrollo.dto.EstudianteResponseRegisterDTO;
import com.example.desarrollo.model.Estudiante;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setPreferNestedProperties(false);

        return modelMapper;
    }
}
