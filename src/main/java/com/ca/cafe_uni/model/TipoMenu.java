package com.ca.cafe_uni.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "TIPO_MENU")
public class TipoMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_menu")
    private Integer idTipoMenu;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // Getters y Setters

    public Integer getIdTipoMenu() {
        return idTipoMenu;
    }

    public void setIdTipoMenu(Integer idTipoMenu) {
        this.idTipoMenu = idTipoMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}