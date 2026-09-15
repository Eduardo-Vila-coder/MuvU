package com.example.desarrollo.service;

import com.example.desarrollo.dto.EstudianteRequestRegisterDTO;
import com.example.desarrollo.dto.EstudianteResponseRegisterDTO;
import com.example.desarrollo.dto.UniversidadSimpleDTO;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.EstudianteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final ModelMapper modelMapper;
    private final UniversidadService universidadService;

    public EstudianteService(EstudianteRepository estudianteRepository, ModelMapper modelMapper, UniversidadService universidadService) {
        this.estudianteRepository = estudianteRepository;
        this.modelMapper = modelMapper;
        this.universidadService = universidadService;
    }

    public Estudiante findById(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public List<Estudiante> findAll() {
        return estudianteRepository.findAll();
    }

    public void deleteById(Long id) {
        estudianteRepository.deleteById(id);
    }

    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }


    public EstudianteResponseRegisterDTO registrarEstudiante(EstudianteRequestRegisterDTO request) {
    // Validar que el correo no exista
        if (estudianteRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Validar que la universidad exista
        Long universidadId = request.getUniversidad().getId();
        Universidad universidadExistente = universidadService.findById(universidadId);

        if (universidadExistente == null) {
            throw new IllegalArgumentException("La universidad con ID " + universidadId + " no existe");
        }

        // Mapear del DTO Request a la Entidad
        Estudiante estudiante = modelMapper.map(request, Estudiante.class);
        estudiante.setUniversidad(universidadExistente);

        // Guardar en base de datos
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);

        // Construir la respuesta SIN recursión
        EstudianteResponseRegisterDTO response = new EstudianteResponseRegisterDTO();
        response.setId(estudianteGuardado.getId());
        response.setNombre(estudianteGuardado.getNombre());
        response.setCorreo(estudianteGuardado.getCorreo());
        response.setVerificado(estudianteGuardado.getVerificado());

        // Crear el DTO simple para la universidad
        UniversidadSimpleDTO universidadDTO = new UniversidadSimpleDTO();
        universidadDTO.setId(estudianteGuardado.getUniversidad().getId());
        universidadDTO.setNombre(estudianteGuardado.getUniversidad().getNombre());
        universidadDTO.setDireccion(estudianteGuardado.getUniversidad().getDireccion());

        response.setUniversidad(universidadDTO);

        return response;
    }
}

