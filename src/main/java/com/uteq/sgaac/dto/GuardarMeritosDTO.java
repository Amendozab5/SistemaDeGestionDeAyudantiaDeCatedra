package com.uteq.sgaac.dto;

import java.math.BigDecimal;

public class GuardarMeritosDTO {

    private BigDecimal puntajeTotal;
    private BigDecimal puntosCalificacionAsignatura;
    private BigDecimal puntosPromedioGeneral;
    private BigDecimal puntosExperiencia;
    private BigDecimal puntosEventos;

    // Getters y Setters

    public BigDecimal getPuntosCalificacionAsignatura() {
        return puntosCalificacionAsignatura;
    }

    public void setPuntosCalificacionAsignatura(BigDecimal puntosCalificacionAsignatura) {
        this.puntosCalificacionAsignatura = puntosCalificacionAsignatura;
    }

    public BigDecimal getPuntosPromedioGeneral() {
        return puntosPromedioGeneral;
    }

    public void setPuntosPromedioGeneral(BigDecimal puntosPromedioGeneral) {
        this.puntosPromedioGeneral = puntosPromedioGeneral;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public BigDecimal getPuntosExperiencia() {
        return puntosExperiencia;
    }

    public void setPuntosExperiencia(BigDecimal puntosExperiencia) {
        this.puntosExperiencia = puntosExperiencia;
    }

    public BigDecimal getPuntosEventos() {
        return puntosEventos;
    }

    public void setPuntosEventos(BigDecimal puntosEventos) {
        this.puntosEventos = puntosEventos;
    }
}
