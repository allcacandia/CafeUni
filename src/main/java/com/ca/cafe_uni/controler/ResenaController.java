package com.ca.cafe_uni.controler;

import com.ca.cafe_uni.model.Resena;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.service.ResenaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;
    private final DetalleMenuRepository detalleMenuRepository;

    public ResenaController(ResenaService resenaService, DetalleMenuRepository detalleMenuRepository) {
        this.resenaService = resenaService;
        this.detalleMenuRepository = detalleMenuRepository;
    }

    @GetMapping
    public String listar(Model model,
                         @RequestParam(required = false, defaultValue = "semana") String filtro) {
        if ("todas".equals(filtro)) {
            model.addAttribute("resenas", resenaService.listarTodas());
        } else {
            model.addAttribute("resenas", resenaService.listarPorSemana());
        }
        model.addAttribute("filtro", filtro);
        return "resena";
    }

    @GetMapping("/nueva/{idDetalle}")
    public String formulario(@PathVariable Integer idDetalle, Model model) {
        model.addAttribute("resena", new Resena());
        model.addAttribute("idDetalle", idDetalle);
        detalleMenuRepository.findById(idDetalle).ifPresent(detalle ->
                model.addAttribute("detalle", detalle)
        );

        return "hacerResena";
    }

    @PostMapping("/guardar/{idDetalle}")
    public String guardar(@PathVariable Integer idDetalle,
                          @ModelAttribute Resena resena,
                          Model model) {
        resenaService.guardar(resena, idDetalle);
        return "redirect:/menu?enviado=true";
    }
}
