package com.uteq.sgaac.dto;

import java.math.BigDecimal;

public class FinalizacionDetalleDTO {

    private String nombreEstudiante;
    private String nombreAsignatura;
    private BigDecimal puntajeMeritos;
    private BigDecimal puntajeOposicion;
    private BigDecimal puntajeTotal;

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters"> 
    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public BigDecimal getPuntajeMeritos() {
        return puntajeMeritos;
    }

    public void setPuntajeMeritos(BigDecimal puntajeMeritos) {
        this.puntajeMeritos = puntajeMeritos;
    }

    public BigDecimal getPuntajeOposicion() {
        return puntajeOposicion;
    }

    public void setPuntajeOposicion(BigDecimal puntajeOposicion) {
        this.puntajeOposicion = puntajeOposicion;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }
    //</editor-fold>
}
