package com.example.desarrollo.service;

import com.example.desarrollo.dto.UniversidadRequestDTO;
import com.example.desarrollo.dto.UniversidadResponseDTO;
import com.example.desarrollo.model.Universidad;
import com.example.desarrollo.repository.UniversidadRepository;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return modelMapper.map(universidad, UniversidadResponseDTO.class);
    }

    @Transactional
    public Universidad save(Universidad uni) {
        if (uni != null && uni.getNombre() != null && !uni.getNombre().isEmpty()) {
            geolocalizarUniversidad(uni);
            return universidadRepository.save(uni);
        } else {
            throw new IllegalArgumentException("Nombre no puede ser nulo");
        }
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

    public Universidad findById(Long id) {
        return universidadRepository.findById(id).orElse(null);
    }

    public List<Universidad> findAll() {
        return universidadRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        universidadRepository.deleteById(id);
    }
}