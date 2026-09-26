package com.example.desarrollo.service;

import com.example.desarrollo.exceptions.ExternalServiceException;
import com.example.desarrollo.exceptions.InvalidOperationException;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class GoogleMapsServiceTest {

    private final GoogleMapsService googleMapsService = new GoogleMapsService();

    @Test
    void devuelveLasCoordenadasDelPrimerResultado() throws Exception {
        GeocodingResult resultado = new GeocodingResult();
        resultado.geometry = new Geometry();
        resultado.geometry.location = new LatLng(-12.13, -77.02);

        LatLng coords = geocodificar(new GeocodingResult[]{resultado});

        assertEquals(-12.13, coords.lat);
        assertEquals(-77.02, coords.lng);
    }

    @Test
    void sinResultados_lanzaInvalidOperation() {
        assertThrows(InvalidOperationException.class, () -> geocodificar(new GeocodingResult[0]));
    }

    @Test
    void siGoogleFalla_lanzaExternalService() throws Exception {
        GeocodingApiRequest request = mock(GeocodingApiRequest.class);
        when(request.await()).thenThrow(new IOException("timeout"));

        try (MockedStatic<GeocodingApi> api = mockStatic(GeocodingApi.class)) {
            api.when(() -> GeocodingApi.geocode(any(), anyString())).thenReturn(request);

            assertThrows(ExternalServiceException.class, () -> googleMapsService.obtenerCoordenadas("Av. Grau 100"));
        }
    }

    private LatLng geocodificar(GeocodingResult[] resultados) throws Exception {
        GeocodingApiRequest request = mock(GeocodingApiRequest.class);
        when(request.await()).thenReturn(resultados);

        try (MockedStatic<GeocodingApi> api = mockStatic(GeocodingApi.class)) {
            api.when(() -> GeocodingApi.geocode(any(), anyString())).thenReturn(request);
            return googleMapsService.obtenerCoordenadas("Jr. Medrano Silva 165");
        }
    }
}
