package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.Visita;
import com.ca.cafe_uni.repository.VisitaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;
    private static final Logger logger = LoggerFactory.getLogger(VisitaService.class);


    public VisitaService(VisitaRepository visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public void registrar(HttpServletRequest request) {
        String ip = obtenerIp(request);
        String userAgentCrudo = request.getHeader("User-Agent");
        String dispositivo = resolverDispositivo(userAgentCrudo);

        logger.info("User-Agent crudo: {}", userAgentCrudo);
        logger.info("Nueva visita registrada - IP: {}, Dispositivo: {}", ip, dispositivo);

        Visita visita = new Visita();
        visita.setIpUsuario(ip);
        visita.setDispositivo(dispositivo);
        visita.setFechaHora(LocalDateTime.now());

        visitaRepository.save(visita);
    }

    public long contarVisitas() {
        return visitaRepository.count();
    }

    private String obtenerIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String resolverDispositivo(String userAgent) {
        if (userAgent == null) return "Desconocido";
        String ua = userAgent.toLowerCase();
        if (ua.contains("android")) return "Movil-Android";
        if (ua.contains("iphone")) return "Movil-iPhone";
        if (ua.contains("tablet") || ua.contains("ipad")) return "Tablet";
        return "Escritorio";
    }
}
