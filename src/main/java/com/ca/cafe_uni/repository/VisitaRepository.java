package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Integer> {

    @Query("SELECT FUNCTION('DATE', v.fechaHora) as dia, COUNT(v) as total " +
            "FROM Visita v " +
            "WHERE v.fechaHora >= :desde " +
            "GROUP BY FUNCTION('DATE', v.fechaHora) " +
            "ORDER BY dia ASC")
    List<Object[]> contarVisitasPorDia(@Param("desde") LocalDateTime desde);

    @Query("SELECT v.dispositivo, COUNT(v) FROM Visita v GROUP BY v.dispositivo")
    List<Object[]> contarPorDispositivo();
}
