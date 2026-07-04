package com.ca.cafe_uni.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private static final int MAX_INTENTOS = 2;
    private static final int BLOQUEO_SEGUNDOS = 30;

    private final Map<String, Integer> intentos = new HashMap<>();
    private final Map<String, LocalDateTime> tiempoBloqueo = new HashMap<>();

    public boolean estaBloqueado(String ip) {
        if (!tiempoBloqueo.containsKey(ip)) return false;

        LocalDateTime tiempoDesbloqueo = tiempoBloqueo.get(ip).plusSeconds(BLOQUEO_SEGUNDOS);
        if (LocalDateTime.now().isAfter(tiempoDesbloqueo)) {
            // Ya pasaron los 30 segundos, limpiar
            intentos.remove(ip);
            tiempoBloqueo.remove(ip);
            return false;
        }
        return true;
    }

    public void loginFallido(String ip) {
        int total = intentos.getOrDefault(ip, 0) + 1;
        intentos.put(ip, total);
        if (total >= MAX_INTENTOS) {
            tiempoBloqueo.put(ip, LocalDateTime.now());
        }
    }

    public void loginExitoso(String ip) {
        intentos.remove(ip);
        tiempoBloqueo.remove(ip);
    }

    public long segundosRestantes(String ip) {
        if (!tiempoBloqueo.containsKey(ip)) return 0;
        LocalDateTime desbloqueo = tiempoBloqueo.get(ip).plusSeconds(BLOQUEO_SEGUNDOS);
        return java.time.Duration.between(LocalDateTime.now(), desbloqueo).getSeconds();
    }
}