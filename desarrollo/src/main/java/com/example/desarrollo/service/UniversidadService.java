package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.exceptions.ResourceNotFoundException;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.UniversidadRepository;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversidadService {

    private final GoogleMapsService googleMapsService;
    private final UniversidadRepository universidadRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public UniversidadResponseDTO createUniversidad(UniversidadRequestDTO universidadRequestDTO) {
        Universidad universidad = modelMapper.map(universidadRequestDTO, Universidad.class);

        geolocalizarUniversidad(universidad);

        universidad = universidadRepository.save(universidad);
        log.info("Universidad {} creada: {}", universidad.getId(), universidad.getNombre());
        return modelMapper.map(universidad, UniversidadResponseDTO.class);
    }

    private void geolocalizarUniversidad(Universidad universidad) {
        // Prioriza la dirección; si no existe, busca directamente por el nombre de la universidad
        String busqueda = (universidad.getDireccion() != null && !universidad.getDireccion().isBlank())
                ? universidad.getDireccion()
                : universidad.getNombre();

        if (busqueda != null && !busqueda.isBlank()) {
            LatLng coords = googleMapsService.obtenerCoordenadas(busqueda);
            universidad.setLatitud(coords.lat);
            universidad.setLongitud(coords.lng);
        }
    }

    public List<UniversidadResponseDTO> findAllDTO() {
        return universidadRepository.findAll()
                .stream()
                .map(uni -> modelMapper.map(uni, UniversidadResponseDTO.class))
                .collect(Collectors.toList());
    }

    public UniversidadResponseDTO findByIdDTO(Long id) {
        return universidadRepository.findById(id)
                .map(uni -> modelMapper.map(uni, UniversidadResponseDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada con ID: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!universidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Universidad no encontrada con ID: " + id);
        }
        universidadRepository.deleteById(id);
        log.info("Universidad {} eliminada", id);
    }
}