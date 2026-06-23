package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.MenuDiario;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.MenuDiarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuDiarioRepository menuDiarioRepository;
    private final DetalleMenuRepository detalleMenuRepository;

    public MenuService(MenuDiarioRepository menuDiarioRepository, DetalleMenuRepository detalleMenuRepository) {
        this.menuDiarioRepository = menuDiarioRepository;
        this.detalleMenuRepository = detalleMenuRepository;
    }

    public Optional<MenuDiario> obtenerMenuHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        return  menuDiarioRepository.findFirstByFechaBetween(inicio, fin);
    }

    public List<DetalleMenu> obtenerDetallesDisponibles(Integer idMenu) {
        return detalleMenuRepository.findByMenuDiario_IdMenuAndDisponibleTrue(idMenu);
    }
}
