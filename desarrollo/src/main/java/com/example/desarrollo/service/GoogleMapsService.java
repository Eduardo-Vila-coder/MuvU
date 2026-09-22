package com.example.desarrollo.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, direccion).await();
            if (results != null && results.length > 0) {
                return results[0].geometry.location;
            }
            throw new RuntimeException("No se encontraron coordenadas para la dirección ingresada: " + direccion);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la API de Google Maps: " + e.getMessage(), e);
        }
    }
}
