package com.ca.cafe_uni.controler;

import com.ca.cafe_uni.model.Resena;
import com.ca.cafe_uni.service.ResenaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @GetMapping
    public String listar(Model model){
        model.addAttribute("resenas", resenaService.listarTodas());
        return "resenas";
    }

    @GetMapping("/nueva/{idDetalle}")
    public String formulario(@PathVariable Integer idDetalle, Model model){
        model.addAttribute("resenas", new Resena());
        model.addAttribute("idDetalle", idDetalle);
        return "formulario-resena";
    }

    @PostMapping("/guardar/{idDetalle}")
    public String guardar(@PathVariable Integer idDetalle,
                          @ModelAttribute Resena resena,
                          Model model){
        resenaService.guardar(resena, idDetalle);
        return "redirect:/resenas";
    }
}
