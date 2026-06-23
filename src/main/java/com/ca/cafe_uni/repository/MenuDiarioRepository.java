package com.ca.cafe_uni.repository;

import com.ca.cafe_uni.model.MenuDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MenuDiarioRepository extends JpaRepository<MenuDiario, Integer> {

    Optional<MenuDiario> findFirstByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
