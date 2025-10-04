package com.uteq.sgaac.model;

import jakarta.persistence.*;

@Entity
@Table(name = "requisito")
public class Requisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisito")
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(length = 50, nullable = false)
    private String tipo;

    @Column(name = "url_plantilla", length = 255)
    private String urlPlantilla;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlPlantilla() {
        return urlPlantilla;
    }

    public void setUrlPlantilla(String urlPlantilla) {
        this.urlPlantilla = urlPlantilla;
    }
}