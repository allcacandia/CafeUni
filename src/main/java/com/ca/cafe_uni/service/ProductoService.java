package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.DetalleMenu;
import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.repository.DetalleMenuRepository;
import com.ca.cafe_uni.repository.ProductoRepository;
import com.ca.cafe_uni.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final DetalleMenuRepository detalleMenuRepository;
    private final ResenaRepository resenaRepository;

    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public void actualizar(Integer id, Producto datosNuevos, MultipartFile imagen) throws IOException {
        Producto producto = obtenerPorId(id);
        producto.setNombre(datosNuevos.getNombre());
        producto.setCategoria(datosNuevos.getCategoria());

        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = imagen.getOriginalFilename();
            String rutaBase = System.getProperty("user.dir");
            Path ruta = Paths.get(rutaBase + "/src/main/resources/static/images/" + nombreArchivo);
            Files.createDirectories(ruta.getParent());
            Thumbnails.of(imagen.getInputStream()).size(800, 800).outputQuality(0.8).toFile(ruta.toFile());
            producto.setImagen(nombreArchivo);
        }

        productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        // Cascada manual: borrar reseñas -> detalles -> producto
        List<DetalleMenu> detalles = detalleMenuRepository.findByProducto_IdProducto(id);
        for (DetalleMenu detalle : detalles) {
            resenaRepository.deleteAll(resenaRepository.findByDetalleMenu_IdDetalle(detalle.getIdDetalle()));
        }
        detalleMenuRepository.deleteAll(detalles);
        productoRepository.deleteById(id);
    }
    public List<Producto> listarTodos(){
        return productoRepository.findAll();
    }

    public void guardar(Producto producto) {
        productoRepository.save(producto);
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(
                Producto.CategoriaProducto.valueOf(categoria)
        );
    }
}
