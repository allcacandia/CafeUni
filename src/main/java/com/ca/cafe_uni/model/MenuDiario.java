package com.ca.cafe_uni.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MENU_DIARIO")
public class MenuDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_menu")
    private Integer idMenu;

    @Column(name= "fecha", nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "menuDiario")
    private List<DetalleMenu> detalles;

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }
}
