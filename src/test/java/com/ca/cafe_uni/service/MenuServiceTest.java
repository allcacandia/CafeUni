package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.MenuDiario;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.MenuDiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDiarioRepository menuDiarioRepository;

    @Mock
    private DetalleMenuRepository detalleMenuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void obtenerMenuHoy_cuandoExisteMenu_debeRetornarlo() {
        // ARRANGE — preparar datos de prueba
        MenuDiario menuFalso = new MenuDiario();
        menuFalso.setIdMenu(1);
        menuFalso.setFecha(LocalDateTime.now());

        when(menuDiarioRepository.findFirstByFechaBetween(any(), any()))
                .thenReturn(Optional.of(menuFalso));

        // ACT — ejecutar el metodo
        Optional<MenuDiario> resultado = menuService.obtenerMenuHoy();

        // ASSERT — verificar el resultado
        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getIdMenu());
    }

    @Test
    void obtenerMenuHoy_cuandoNoExisteMenu_debeRetornarVacio() {
        // ARRANGE
        when(menuDiarioRepository.findFirstByFechaBetween(any(), any()))
                .thenReturn(Optional.empty());

        // ACT
        Optional<MenuDiario> resultado = menuService.obtenerMenuHoy();

        // ASSERT
        assertFalse(resultado.isPresent());
    }

    @Test
    void obtenerDetallesDisponibles_debeRetornarSoloDisponibles() {
        // ARRANGE
        DetalleMenu detalle1 = new DetalleMenu();
        detalle1.setIdDetalle(1);
        detalle1.setDisponible(true);

        when(detalleMenuRepository.findByMenuDiario_IdMenuAndDisponibleTrue(1))
                .thenReturn(List.of(detalle1));

        // ACT
        List<DetalleMenu> resultado = menuService.obtenerDetallesDisponibles(1);

        // ASSERT
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getDisponible());
    }
}