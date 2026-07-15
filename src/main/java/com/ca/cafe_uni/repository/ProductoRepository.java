package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.Producto;
import com.ca.cafe_uni.model.TipoMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(Producto.CategoriaProducto categoria);

}
