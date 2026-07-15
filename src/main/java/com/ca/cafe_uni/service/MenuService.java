package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.MenuDiario;
import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.model.TipoMenu;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.MenuDiarioRepository;
import com.ca.cafe_uni.repository.ProductoRepository;
import com.ca.cafe_uni.repository.TipoMenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuDiarioRepository menuDiarioRepository;
    private final DetalleMenuRepository detalleMenuRepository;
    private final TipoMenuRepository tipoMenuRepository;
    private final ProductoRepository productoRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    public MenuService(MenuDiarioRepository menuDiarioRepository,
                       DetalleMenuRepository detalleMenuRepository,
                       TipoMenuRepository tipoMenuRepository,
                       ProductoRepository productoRepository) {
        this.menuDiarioRepository = menuDiarioRepository;
        this.detalleMenuRepository = detalleMenuRepository;
        this.tipoMenuRepository = tipoMenuRepository;
        this.productoRepository = productoRepository;
    }

    public Optional<MenuDiario> obtenerMenuHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        Optional<MenuDiario> resultado = menuDiarioRepository.findFirstByFechaBetween(inicio, fin);

        if (resultado.isPresent()) {
            logger.info("Menú del día encontrado: id={}", resultado.get().getIdMenu());
        } else {
            logger.warn("No se encontró menú para el día de hoy: {}", hoy);
        }
        return resultado;
    }

    public boolean existeMenuHoy() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        boolean existe = menuDiarioRepository.findFirstByFechaBetween(inicio, fin).isPresent();
        logger.info("Verificación de menú para hoy {}: existe={}", hoy, existe);
        return existe;
    }

    public List<DetalleMenu> obtenerDetallesDisponibles(Integer idMenu) {
        List<DetalleMenu> detalles = detalleMenuRepository.findByMenuDiario_IdMenuAndDisponibleTrue(idMenu);
        logger.info("Detalles disponibles para idMenu={}: {} platos", idMenu, detalles.size());
        return detalles;
    }

    public void publicarMenu(List<Integer> ejecutivoIds,
                             List<Integer> estudiantilIds,
                             List<Integer> entradaIds,
                             List<Integer> postreIds) {

        logger.info("Publicando nuevo menú diario para la fecha: {}", LocalDateTime.now());

        MenuDiario menu = new MenuDiario();
        menu.setFecha(LocalDateTime.now());
        menuDiarioRepository.save(menu);
        logger.info("MenuDiario creado con id={}", menu.getIdMenu());

        TipoMenu tipoEjecutivo = tipoMenuRepository.findByNombre("ejecutivo");
        TipoMenu tipoEstudiantil = tipoMenuRepository.findByNombre("estudiantil");

        guardarDetalles(menu, ejecutivoIds, tipoEjecutivo);
        guardarDetalles(menu, estudiantilIds, tipoEstudiantil);
        if (entradaIds != null) guardarDetalles(menu, entradaIds, null);
        if (postreIds != null) guardarDetalles(menu, postreIds, null);

        logger.info("Menú publicado correctamente con {} detalles en total",
                ejecutivoIds.size() + estudiantilIds.size() +
                        (entradaIds != null ? entradaIds.size() : 0) +
                        (postreIds != null ? postreIds.size() : 0));
    }

    private void guardarDetalles(MenuDiario menu, List<Integer> ids, TipoMenu tipo) {
        for (Integer idProducto : ids) {
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> {
                        logger.error("Producto no encontrado con id={}", idProducto);
                        return new IllegalArgumentException("Producto no encontrado: " + idProducto);
                    });
            DetalleMenu detalle = new DetalleMenu();
            detalle.setMenuDiario(menu);
            detalle.setProducto(producto);
            detalle.setTipoMenu(tipo);
            detalle.setDisponible(true);
            detalleMenuRepository.save(detalle);

            String nombreTipo = (tipo != null) ? tipo.getNombre() : "sin tipo (entrada/postre)";
            logger.info("Detalle guardado - producto={}, tipo={}", producto.getNombre(), nombreTipo);
        }
    }
}