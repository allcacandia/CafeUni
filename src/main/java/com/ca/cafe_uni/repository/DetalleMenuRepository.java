package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.DetalleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMenuRepository extends JpaRepository<DetalleMenu,Integer> {

    List<DetalleMenu> findByMenuDiario_IdMenuAndDisponibleTrue(Integer idMenu);


}
