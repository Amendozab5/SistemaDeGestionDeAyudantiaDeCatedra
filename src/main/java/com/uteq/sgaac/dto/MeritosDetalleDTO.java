package com.uteq.sgaac.dto;

import java.math.BigDecimal;
import java.util.List;

// Este DTO transportará los datos para el modal de evaluación de méritos.
public class MeritosDetalleDTO {

    private String nombreEstudiante;
    private String nombreAsignatura;

    // Desglose de puntos para cargar en el modal (si ya fue guardado)
    private BigDecimal puntosCalificacionAsignatura;
    private BigDecimal puntosPromedioGeneral;
    private BigDecimal puntosExperiencia;
    private BigDecimal puntosEventos;

    private List<PostulacionDocumentoDTO> documentos;
    private List<DocumentoMeritoDTO> documentosMerito;

    // --- Getters y Setters ---


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

    public List<DocumentoMeritoDTO> getDocumentosMerito() {
        return documentosMerito;
    }

    public void setDocumentosMerito(List<DocumentoMeritoDTO> documentosMerito) {
        this.documentosMerito = documentosMerito;
    }

    public List<PostulacionDocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<PostulacionDocumentoDTO> documentos) {
        this.documentos = documentos;
    }

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
