package com.uteq.sgaac.dto;

public class RequisitoDTO {
    private Long id;
    private String descripcion;
    private boolean activo;
    private String tipo;
    private String urlPlantilla;

    public RequisitoDTO(Long id, String descripcion, boolean activo, String tipo, String urlPlantilla) {
        this.id = id;
        this.descripcion = descripcion;
        this.activo = activo;
        this.tipo = tipo;
        this.urlPlantilla = urlPlantilla;
    }

    // Getters and setters
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