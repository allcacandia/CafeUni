package com.ca.cafe_uni.controler;

import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/gestion-interna")
public class AdminController {

    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;
    private final ProductoService productoService;
    private final MenuService menuService;
    private final ReporteService reporteService;
    private final ReporteExcelService reporteExcelService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    public AdminController(LoginAttemptService loginAttemptService, HttpServletRequest request, ProductoService productoService, MenuService menuService, ReporteService reporteService, ReporteExcelService reporteExcelService) {
        this.loginAttemptService = loginAttemptService;
        this.request = request;
        this.productoService = productoService;
        this.menuService = menuService;
        this.reporteService = reporteService;
        this.reporteExcelService = reporteExcelService;
    }

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

    @GetMapping("/producto")
    public String producto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", Producto.CategoriaProducto.values());
        return "admin/producto";
    }

    @PostMapping("/producto/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto,
                                  BindingResult result,
                                  @RequestParam("file") MultipartFile imagen,
                                  Model model) throws IOException {

        logger.info("Guardando nuevo producto: {}", producto.getNombre());

        if (imagen.isEmpty()) {
            result.rejectValue("imagen", "error.imagen", "La imagen es obligatorio");
        }

        if (result.hasErrors()) {
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", Producto.CategoriaProducto.values());
            return "admin/producto";
        }

        String nombreArchivo = imagen.getOriginalFilename();
        String rutaBase = System.getProperty("user.dir");
        Path ruta = Paths.get(rutaBase + "/src/main/resources/static/images/" + nombreArchivo);
        Files.createDirectories(ruta.getParent());

        try {
            Thumbnails.of(imagen.getInputStream())
                    .size(800, 800)
                    .outputQuality(0.8)
                    .toFile(ruta.toFile());
        } catch (Exception e) {
            logger.error("Error al procesar la imagen: {}", nombreArchivo, e);
            result.rejectValue("imagen", "error.imagen",
                    "El archivo de imagen no es válido. Asegúrate de que sea un JPG, PNG o GIF real.");
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", Producto.CategoriaProducto.values());
            return "admin/producto";
        }

        producto.setImagen(nombreArchivo);
        productoService.guardar(producto);

        return "redirect:/gestion-interna/producto?guardado=true";
    }

    @GetMapping("/publicarMenu")
    public String menu(Model model) {
        model.addAttribute("menuExiste", menuService.existeMenuHoy());
        model.addAttribute("platos", productoService.listarPorCategoria("plato"));
        model.addAttribute("entradas", productoService.listarPorCategoria("entrada"));
        model.addAttribute("postres", productoService.listarPorCategoria("postre"));
        return "admin/publicarMenu";
    }

    @PostMapping("/publicarMenu/publicar")
    public String publicarMenu(@RequestParam List<Integer> ejecutivo,
                               @RequestParam List<Integer> estudiantil,
                               @RequestParam List<Integer> entrada,
                               @RequestParam List<Integer> postre) {

        logger.info("Admin publicando menú del día");

        menuService.publicarMenu(ejecutivo, estudiantil, entrada, postre);
        return "redirect:/gestion-interna/publicarMenu?publicado=true";
    }

    @GetMapping("/reportes")
    public String reportes(Model model) {
        model.addAttribute("mejoresCalificados", reporteService.mejoresCalificadosHoy());
        model.addAttribute("visitasSemana", reporteService.visitasUltimos7Dias());
        return "admin/reportes";
    }

    @GetMapping("/reportes/excel")
    public ResponseEntity<byte[]> descargarReporteExcel() throws IOException {
        byte[] excelBytes = reporteExcelService.generarReporteCompleto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "reporte-cafeteria.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("mejoresCalificados", reporteService.mejoresCalificadosHoy());
        model.addAttribute("mejoresCalificadosSemana", reporteService.mejoresCalificadosSemana());
        model.addAttribute("visitasSemana", reporteService.visitasUltimos7Dias());
        model.addAttribute("visitasPorDispositivo", reporteService.visitasPorDispositivo());
        return "admin/dashboard";
    }
}