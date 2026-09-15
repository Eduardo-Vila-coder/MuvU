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
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(EstudianteRequestDTO.class, Estudiante.class)
                .addMappings(m -> m.skip(Estudiante::setUniversidad));
        modelMapper.typeMap(CalificacionRequestDTO.class, Calificacion.class)
                .addMappings(m -> {
                    m.skip(Calificacion::setAutor);
                    m.skip(Calificacion::setReceptor);
                    m.skip(Calificacion::setReserva);
                });
        return modelMapper;
    }
}
