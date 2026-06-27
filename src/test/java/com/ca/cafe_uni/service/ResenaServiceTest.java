package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.Resena;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.ResenaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private DetalleMenuRepository detalleMenuRepository;

    @InjectMocks
    private ResenaService resenaService;

    @Test
    void listarTodas_debeRetornarTodasLasResenas() {
        // ARRANGE
        Resena resena1 = new Resena();
        resena1.setCalificacionPlato(5);
        resena1.setCalificacionServicio(4);

        Resena resena2 = new Resena();
        resena2.setCalificacionPlato(3);
        resena2.setCalificacionServicio(3);

        when(resenaRepository.findAll()).thenReturn(List.of(resena1, resena2));

        // ACT
        List<Resena> resultado = resenaService.listarTodas();

        // ASSERT
        assertEquals(2, resultado.size());
    }

    @Test
    void guardar_cuandoDetalleExiste_debeGuardarResena() {
        // ARRANGE
        DetalleMenu detalle = new DetalleMenu();
        detalle.setIdDetalle(1);

        Resena resena = new Resena();
        resena.setCalificacionPlato(5);
        resena.setCalificacionServicio(5);

        when(detalleMenuRepository.findById(1)).thenReturn(Optional.of(detalle));
        when(resenaRepository.save(any())).thenReturn(resena);

        // ACT
        Resena resultado = resenaService.guardar(resena, 1);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacionPlato());
        assertEquals(detalle, resena.getDetalleMenu());
    }

    @Test
    void guardar_cuandoDetalleNoExiste_debeLanzarExcepcion() {
        // ARRANGE
        when(detalleMenuRepository.findById(99)).thenReturn(Optional.empty());

        Resena resena = new Resena();

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () -> {
            resenaService.guardar(resena, 99);
        });
    }
}