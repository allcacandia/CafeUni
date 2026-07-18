package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.MenuDiario;
import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.model.TipoMenu;
import com.ca.cafe_uni.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuDiarioRepository menuDiarioRepository;
    private final DetalleMenuRepository detalleMenuRepository;
    private final TipoMenuRepository tipoMenuRepository;
    private final ProductoRepository productoRepository;
    private final ResenaRepository resenaRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);
    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");

    public MenuService(MenuDiarioRepository menuDiarioRepository, DetalleMenuRepository detalleMenuRepository, TipoMenuRepository tipoMenuRepository, ProductoRepository productoRepository, ResenaRepository resenaRepository) {
        this.menuDiarioRepository = menuDiarioRepository;
        this.detalleMenuRepository = detalleMenuRepository;
        this.tipoMenuRepository = tipoMenuRepository;
        this.productoRepository = productoRepository;
        this.resenaRepository = resenaRepository;
    }

    public Optional<MenuDiario> obtenerMenuHoy() {
        LocalDate hoy = LocalDate.now(ZONA_PERU);
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

    public MenuDiario obtenerMenuHoyConDetalles() {
        LocalDate hoy = LocalDate.now(ZONA_PERU);
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.atTime(23, 59, 59);
        return menuDiarioRepository.findFirstByFechaBetween(inicio, fin).orElse(null);
    }

    public boolean existeMenuHoy() {
        LocalDate hoy = LocalDate.now(ZONA_PERU);
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

        logger.info("Publicando nuevo menú diario para la fecha: {}", LocalDateTime.now(ZONA_PERU));

        MenuDiario menu = new MenuDiario();
        menu.setFecha(LocalDateTime.now(ZONA_PERU));
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

    public void eliminarMenuHoy() {
        MenuDiario menu = obtenerMenuHoyConDetalles();
        if (menu == null) return;

        List<DetalleMenu> detalles = detalleMenuRepository.findByMenuDiario_IdMenu(menu.getIdMenu());
        for (DetalleMenu detalle : detalles) {
            resenaRepository.deleteAll(resenaRepository.findByDetalleMenu_IdDetalle(detalle.getIdDetalle()));
        }
        detalleMenuRepository.deleteAll(detalles);
        menuDiarioRepository.delete(menu);

        logger.info("Menú del día eliminado por el administrador");
    }

    public void actualizarDetalle(Integer idDetalle, Integer idProductoNuevo) {
        DetalleMenu detalle = detalleMenuRepository.findById(idDetalle)
                .orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado"));
        Producto producto = productoRepository.findById(idProductoNuevo)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        detalle.setProducto(producto);
        detalleMenuRepository.save(detalle);
        logger.info("Detalle {} actualizado a producto {}", idDetalle, producto.getNombre());
    }

}