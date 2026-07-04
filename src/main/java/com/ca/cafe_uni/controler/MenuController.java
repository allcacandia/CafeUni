package com.ca.cafe_uni.controler;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.MenuDiario;
import com.ca.cafe_uni.service.MenuService;
import com.ca.cafe_uni.service.VisitaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MenuController {

    private final MenuService menuService;
    private final VisitaService visitaService;

    public MenuController(MenuService menuService, VisitaService visitaService) {
        this.menuService = menuService;
        this.visitaService = visitaService;
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpServletRequest request,
                       @RequestParam(required = false) String enviado) {

            visitaService.registrar(request);

        if ("true".equals(enviado)) {
            model.addAttribute("enviado", true);
        }

        Optional<MenuDiario> menuOpt = menuService.obtenerMenuHoy();

        if (menuOpt.isPresent()) {
            List<DetalleMenu> detalles = menuService.obtenerDetallesDisponibles(
                    menuOpt.get().getIdMenu()
            );
            model.addAttribute("menu", menuOpt.get());
            model.addAttribute("detalles", detalles);
        } else {
            model.addAttribute("sinMenu", true);
        }
        return "menu";
    }
}
