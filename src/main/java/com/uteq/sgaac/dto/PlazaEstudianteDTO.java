package com.uteq.sgaac.dto;

public class PlazaEstudianteDTO {

    private Long plazaId;
    private String asignaturaNombre;
    private String docenteNombre;
    private Integer cuposDisponibles;
    private String carreraNombre;
    private String periodo;
    private String convocatoriaTitulo;

    public PlazaEstudianteDTO(Long plazaId, String asignaturaNombre, String docenteNombre, Integer cuposDisponibles, String carreraNombre, String periodo, String convocatoriaTitulo) {
        this.plazaId = plazaId;
        this.asignaturaNombre = asignaturaNombre;
        this.docenteNombre = docenteNombre;
        this.cuposDisponibles = cuposDisponibles;
        this.carreraNombre = carreraNombre;
        this.periodo = periodo;
        this.convocatoriaTitulo = convocatoriaTitulo;
    }

    // Getters and Setters

    public Long getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(Long plazaId) {
        this.plazaId = plazaId;
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

    public Integer getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(Integer cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public String getCarreraNombre() {
        return carreraNombre;
    }

    public void setCarreraNombre(String carreraNombre) {
        this.carreraNombre = carreraNombre;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getConvocatoriaTitulo() {
        return convocatoriaTitulo;
    }

    public void setConvocatoriaTitulo(String convocatoriaTitulo) {
        this.convocatoriaTitulo = convocatoriaTitulo;
    }
}