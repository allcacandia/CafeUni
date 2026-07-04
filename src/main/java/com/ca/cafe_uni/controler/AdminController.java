package com.ca.cafe_uni.controler;

import com.ca.cafe_uni.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/gestion-interna")
@RequiredArgsConstructor
public class AdminController {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, Model model) {
        String ip = request.getRemoteAddr();

        if (loginAttemptService.estaBloqueado(ip)) {
            model.addAttribute("bloqueado", true);
            model.addAttribute("segundos", loginAttemptService.segundosRestantes(ip));
            return "admin/login";
        }

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos.");
        }
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }
}