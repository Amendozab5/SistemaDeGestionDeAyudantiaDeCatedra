package com.uteq.sgaac.dto;

public class PlazaPendienteDTO {

    private Long plazaId;
    private String convocatoriaTitulo;
    private String coordinadorNombre;
    private String asignaturaNombre;
    private String docenteNombre;
    private Integer cupos;

    public PlazaPendienteDTO(Long plazaId, String convocatoriaTitulo, String coordinadorNombre, String asignaturaNombre, String docenteNombre, Integer cupos) {
        this.plazaId = plazaId;
        this.convocatoriaTitulo = convocatoriaTitulo;
        this.coordinadorNombre = coordinadorNombre;
        this.asignaturaNombre = asignaturaNombre;
        this.docenteNombre = docenteNombre;
        this.cupos = cupos;
    }

    // Getters and Setters
    public Long getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(Long plazaId) {
        this.plazaId = plazaId;
    }

    public String getConvocatoriaTitulo() {
        return convocatoriaTitulo;
    }

    public void setConvocatoriaTitulo(String convocatoriaTitulo) {
        this.convocatoriaTitulo = convocatoriaTitulo;
    }

    public String getCoordinadorNombre() {
        return coordinadorNombre;
    }

    public void setCoordinadorNombre(String coordinadorNombre) {
        this.coordinadorNombre = coordinadorNombre;
    }

    public String getAsignaturaNombre() {
        return asignaturaNombre;
    }

    public void setAsignaturaNombre(String asignaturaNombre) {
        this.asignaturaNombre = asignaturaNombre;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }
}
