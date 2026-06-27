package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.Visita;
import com.ca.cafe_uni.repository.VisitaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitaServiceTest {

    @Mock
    private VisitaRepository visitaRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private VisitaService visitaService;

    @Test
    void registrar_desdeEscritorio_debeGuardarVisita() {
        // ARRANGE
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 Windows NT");

        // ACT
        visitaService.registrar(request);

        // ASSERT
        verify(visitaRepository, times(1)).save(any(Visita.class));
    }

    @Test
    void registrar_desdeAndroid_debeGuardarVisita() {
        // ARRANGE
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.2");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 Android Mobile");

        // ACT
        visitaService.registrar(request);

        // ASSERT
        verify(visitaRepository, times(1)).save(any(Visita.class));
    }

    @Test
    void registrar_desdeIphone_debeGuardarVisita() {
        // ARRANGE
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.3");
        when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 iPhone");

        // ACT
        visitaService.registrar(request);

        // ASSERT
        verify(visitaRepository, times(1)).save(any(Visita.class));
    }

    @Test
    void contarVisitas_debeRetornarTotal() {
        // ARRANGE
        when(visitaRepository.count()).thenReturn(42L);

        // ACT
        long total = visitaService.contarVisitas();

        // ASSERT
        assertEquals(42L, total);
    }
}