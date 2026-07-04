package com.ca.cafe_uni.model;


import jakarta.persistence.*;

@Entity
@Table(name = "RESENA")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena", nullable = false)
    private Integer idResena;

    @ManyToOne
    @JoinColumn(name = "id_detalle", nullable = false)
    private DetalleMenu detalleMenu;

    @Column(name = "calificacion_plato", nullable = false)
    private Integer calificacionPlato;

    @Column(name = "calificacion_servicio", nullable = false)
    private Integer calificacionServicio;

    public Integer getIdResena() {
        return idResena;
    }

    public void setIdResena(Integer idResena) {
        this.idResena = idResena;
    }

    public DetalleMenu getDetalleMenu() {
        return detalleMenu;
    }

    public void setDetalleMenu(DetalleMenu detalleMenu) {
        this.detalleMenu = detalleMenu;
    }

    public Integer getCalificacionPlato() {
        return calificacionPlato;
    }

    public void setCalificacionPlato(Integer calificacionPlato) {
        this.calificacionPlato = calificacionPlato;
    }

    public Integer getCalificacionServicio() {
        return calificacionServicio;
    }

    public void setCalificacionServicio(Integer calificacionServicio) {
        this.calificacionServicio = calificacionServicio;
    }
}
