package com.ca.cafe_uni.service;

import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
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
