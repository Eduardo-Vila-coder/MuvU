package com.example.desarrollo.service;

import com.example.desarrollo.dto.EstudianteRequestDTO;
import com.example.desarrollo.dto.EstudianteResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Estudiante;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.EstudianteRepository;
import com.example.desarrollo.repository.UniversidadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.desarrollo.dto.EstudiantePerfilDTO;
import com.example.desarrollo.dto.EstudianteUpdateRequestDTO;
import com.example.desarrollo.repository.CalificacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;
    private final UniversidadRepository universidadRepository;
    private final ModelMapper modelMapper;
    private final CalificacionRepository calificacionRepository;

    public EstudianteService(EstudianteRepository estudianteRepository,
                             UniversidadRepository universidadRepository,
                             ModelMapper modelMapper, CalificacionRepository calificacionRepository){
        this.estudianteRepository = estudianteRepository;
        this.universidadRepository = universidadRepository;
        this.modelMapper = modelMapper;
        this.calificacionRepository = calificacionRepository;
    }

    // POST (Crear estudiante)
    public EstudianteResponseDTO createEstudiante(EstudianteRequestDTO estudianteRequestDTO){
        Universidad universidad = universidadRepository.findById(estudianteRequestDTO.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada")); // ResponseStatusException(HttpStatus.NOT_FOUND, "Universidad no encontrada")
        Estudiante estudiante = modelMapper.map(estudianteRequestDTO, Estudiante.class);
        estudiante.setUniversidad(universidad);

        return modelMapper.map(estudianteRepository.save(estudiante), EstudianteResponseDTO.class);
    }

    // PUT (Actualizar estudiante)
    public EstudianteResponseDTO getById(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
// ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado")
        return modelMapper.map(estudiante, EstudianteResponseDTO.class);
    }

    // GET (Obtener todos los estudiantes paginados)
    public Page<EstudianteResponseDTO> getAll(Pageable pageable) {
        return estudianteRepository.findAll(pageable)
                .map(e -> modelMapper.map(e, EstudianteResponseDTO.class));
    }

    // PUT (Actualizar estudiante [cambio de universidad])
    public EstudianteResponseDTO updateEstudiante(Long id, EstudianteUpdateRequestDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
// ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado")
        Universidad universidad = universidadRepository.findById(dto.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrado"));
//ResponseStatusException(HttpStatus.NOT_FOUND, "Universidad no encontrada")
        estudiante.setNombre(dto.getNombre());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setUniversidad(universidad);

        return modelMapper.map(estudianteRepository.save(estudiante), EstudianteResponseDTO.class);
    }

    // GET (Informacion relevante para Arrendador)
    public EstudiantePerfilDTO getPerfil(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
// ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado")
        EstudiantePerfilDTO perfil = modelMapper.map(estudiante, EstudiantePerfilDTO.class);
        perfil.setPuntuacionPromedio(calificacionRepository.findPromedioByReceptorId(id));
        perfil.setTotalCalificaciones(calificacionRepository.countByReceptorId(id));

        return perfil;
    }

    // DELETE (Eliminar estudiante)
    public void delete(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado");
            // ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado")
        }
        estudianteRepository.deleteById(id);
    }
}
