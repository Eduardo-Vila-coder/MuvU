package com.example.desarrollo.service;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.exceptions.ExternalServiceException;
import com.example.desarrollo.exceptions.InvalidOperationException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private GeoApiContext context;

    @PostConstruct
    public void init() {
        this.context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public LatLng obtenerCoordenadas(String direccion) {
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context, direccion).await();
        } catch (Exception e) {
            log.error("Falló la consulta a Google Maps para '{}'", direccion, e);
            throw new ExternalServiceException("Error al consultar la API de Google Maps: " + e.getMessage(), e);
        }
        if (results == null || results.length == 0) {
            log.warn("Google Maps no encontró coordenadas para '{}'", direccion);
            throw new InvalidOperationException("No se encontraron coordenadas para la dirección: " + direccion);
        }
        return results[0].geometry.location;
    }
}
