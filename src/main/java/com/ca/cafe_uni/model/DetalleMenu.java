package com.ca.cafe_uni.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "DETALLE_MENU")
public class DetalleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_menu", nullable = false)
    private MenuDiario menuDiario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_tipo_menu")
    private TipoMenu tipoMenu;

    @Column(name = "disponible", nullable = false)
    private Boolean disponible = true;

    @OneToMany(mappedBy = "detalleMenu")
    private List<Resena> resenas;

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public MenuDiario getMenuDiario() {
        return menuDiario;
    }

    public void setMenuDiario(MenuDiario menuDiario) {
        this.menuDiario = menuDiario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoMenu getTipoMenu() {
        return tipoMenu;
    }

    public void setTipoMenu(TipoMenu tipoMenu) {
        this.tipoMenu = tipoMenu;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public List<Resena> getResenas() {
        return resenas;
    }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }
}
