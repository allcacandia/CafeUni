package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.Resena;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.ResenaRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final DetalleMenuRepository detalleMenuRepository;

    public ResenaService(ResenaRepository resenaRepository, DetalleMenuRepository detalleMenuRepository) {
        this.resenaRepository = resenaRepository;
        this.detalleMenuRepository = detalleMenuRepository;
    }

    public List<Resena> listarPorSemana() {
        LocalDateTime inicio = LocalDate.now()
                .with(DayOfWeek.MONDAY)
                .atStartOfDay();
        LocalDateTime fin = LocalDate.now()
                .with(DayOfWeek.SUNDAY)
                .atTime(23, 59, 59);
        return resenaRepository.findByDetalleMenu_MenuDiario_FechaBetween(inicio, fin);
    }

    public List<Resena> listarTodas() {
        return resenaRepository.findAll();
    }

    public List<Resena> listarPorDetalle(Integer idDetalle) {
        return resenaRepository.findByDetalleMenu_IdDetalle(idDetalle);
    }

    public Resena guardar(Resena resena, Integer idDetalle) {
        DetalleMenu detalle = detalleMenuRepository.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado: " + idDetalle));
        resena.setDetalleMenu(detalle);
        return resenaRepository.save(resena);
    }
}
