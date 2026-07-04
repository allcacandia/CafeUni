package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena,Integer> {

    List<Resena> findByDetalleMenu_IdDetalle(Integer idDetalle);

    List<Resena> findByDetalleMenu_MenuDiario_FechaBetween(LocalDateTime inicio, LocalDateTime fin);

}
