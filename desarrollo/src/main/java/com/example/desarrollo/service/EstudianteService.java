package com.example.desarrollo.service;

import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.EstudianteResponseDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.UniversidadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.desarrollo.dto.EstudianteUpdateRequestDTO;
import com.example.desarrollo.repository.CalificacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final UniversidadService universidadService;
    private final ModelMapper modelMapper;
    private final CalificacionRepository calificacionRepository;

    public EstudianteService(EstudianteRepository estudianteRepository,
                             UniversidadService universidadService,
                             ModelMapper modelMapper, CalificacionRepository calificacionRepository){
        this.estudianteRepository = estudianteRepository;
        this.universidadService = universidadService;
        this.modelMapper = modelMapper;
        this.calificacionRepository = calificacionRepository;
    }

    // POST (Crear estudiante)
    public EstudianteResponseDTO createEstudiante(EstudianteRequestDTO estudianteRequestDTO){
        Universidad universidad = universidadService.findById(estudianteRequestDTO.getUniversidad().getId());
        if (universidad == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Universidad no encontrada");
        }
        Estudiante estudiante = modelMapper.map(estudianteRequestDTO, Estudiante.class);
        estudiante.setUniversidad(universidad);
        estudianteRepository.save(estudiante);

        EstudianteResponseDTO estudianteResponseDTO = modelMapper.map(estudiante, EstudianteResponseDTO.class);
        estudianteResponseDTO.setUniversidad(modelMapper.map(universidad, UniversidadResponseDTO.class));
        return estudianteResponseDTO;
    }


    // GET (Obtener todos los estudiantes paginados)
    public Page<EstudianteResponseDTO> getAll(Pageable pageable) {
        return estudianteRepository.findAll(pageable)
                .map(e -> modelMapper.map(e, EstudianteResponseDTO.class));
    }

    // PUT (Actualizar estudiante [cambio de universidad])
    public EstudianteResponseDTO updateEstudiante(Long id, EstudianteUpdateRequestDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Estudiante no encontrado"));

        Universidad universidad = universidadService.findById(dto.getUniversidadId());
        if  (universidad == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Universidad no encontrada");
        }
        estudiante.setNombre(dto.getNombre());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setUniversidad(universidad);

        return modelMapper.map(estudianteRepository.save(estudiante), EstudianteResponseDTO.class);
    }

    // GET (Informacion relevante para Arrendador)
    public Estudiante getById(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public EstudianteResponseDTO getPerfil(Long id) {
        Estudiante estudiante = this.getById(id);
        if  (estudiante == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado");
        }
        return modelMapper.map(estudiante, EstudianteResponseDTO.class);
    }

    // DELETE (Eliminar estudiante)
    public void delete(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado");
        }
        estudianteRepository.deleteById(id);
    }
}
