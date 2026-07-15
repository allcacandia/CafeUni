package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena,Integer> {

    List<Resena> findByDetalleMenu_IdDetalle(Integer idDetalle);

    List<Resena> findByDetalleMenu_MenuDiario_FechaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT p.nombre, AVG(r.calificacionPlato) as promedio " +
            "FROM Resena r " +
            "JOIN r.detalleMenu d " +
            "JOIN d.producto p " +
            "JOIN d.menuDiario m " +
            "WHERE m.fecha BETWEEN :inicio AND :fin " +
            "GROUP BY p.nombre " +
            "ORDER BY promedio DESC")
    List<Object[]> mejoresCalificadosPorFecha(@Param("inicio") LocalDateTime inicio,
                                              @Param("fin") LocalDateTime fin);

    @Query("SELECT FUNCTION('DATE', m.fecha) as dia, p.nombre, AVG(r.calificacionPlato) as promedio " +
            "FROM Resena r " +
            "JOIN r.detalleMenu d " +
            "JOIN d.producto p " +
            "JOIN d.menuDiario m " +
            "WHERE m.fecha BETWEEN :inicio AND :fin " +
            "GROUP BY FUNCTION('DATE', m.fecha), p.nombre " +
            "ORDER BY dia ASC, promedio DESC")
    List<Object[]> mejoresCalificadosPorSemana(@Param("inicio") LocalDateTime inicio,
                                               @Param("fin") LocalDateTime fin);

}
