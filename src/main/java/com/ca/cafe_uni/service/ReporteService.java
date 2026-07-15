package com.ca.cafe_uni.service;

import com.ca.cafe_uni.repository.ResenaRepository;
import com.ca.cafe_uni.repository.VisitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ResenaRepository resenaRepository;
    private final VisitaRepository visitaRepository;

    /** Devuelve un mapa {nombrePlato: promedioCalificacion} del día de hoy. */
    public Map<String, Double> mejoresCalificadosHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);

        List<Object[]> resultados = resenaRepository.mejoresCalificadosPorFecha(inicio, fin);

        Map<String, Double> mapa = new LinkedHashMap<>();
        for (Object[] fila : resultados) {
            String nombre = (String) fila[0];
            Double promedio = (Double) fila[1];
            mapa.put(nombre, Math.round(promedio * 10.0) / 10.0);
        }
        return mapa;
    }

    /** Devuelve un mapa {fecha: cantidadVisitas} de los últimos 7 días. */
    public Map<String, Long> visitasUltimos7Dias() {
        LocalDateTime desde = LocalDate.now().minusDays(6).atStartOfDay();
        List<Object[]> resultados = visitaRepository.contarVisitasPorDia(desde);

        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Object[] fila : resultados) {
            String fecha = fila[0].toString();
            Long total = (Long) fila[1];
            mapa.put(fecha, total);
        }
        return mapa;
    }

    /** Devuelve el mejor calificado de cada día de la semana actual (lunes a hoy). */
    public Map<String, String> mejoresCalificadosSemana() {
        LocalDateTime inicio = LocalDate.now().with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);

        List<Object[]> resultados = resenaRepository.mejoresCalificadosPorSemana(inicio, fin);

        // Como la query ya viene ordenada por día y promedio DESC,
        // el primer plato de cada día es el mejor calificado de ese día.
        Map<String, String> mejoresPorDia = new LinkedHashMap<>();
        for (Object[] fila : resultados) {
            String dia = fila[0].toString();
            String plato = (String) fila[1];
            mejoresPorDia.putIfAbsent(dia, plato);
        }
        return mejoresPorDia;
    }

    /** Devuelve un mapa {dispositivo: cantidad} con el total de visitas por tipo de dispositivo. */
    public Map<String, Long> visitasPorDispositivo() {
        List<Object[]> resultados = visitaRepository.contarPorDispositivo();
        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Object[] fila : resultados) {
            String dispositivo = (String) fila[0];
            Long total = (Long) fila[1];
            mapa.put(dispositivo, total);
        }
        return mapa;
    }
}