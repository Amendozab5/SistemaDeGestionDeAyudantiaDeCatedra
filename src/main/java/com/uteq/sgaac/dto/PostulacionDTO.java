package com.uteq.sgaac.dto;

import java.util.Set;

public class PostulacionDTO {
    private Long idPostulacion;
    private String estudianteNombre;
    private String estudianteEmail;
    private String asignaturaNombre;
    private String estadoPostulacion;
    private Set<PostulacionDocumentoDTO> documentos;
    private String convocatoriaTitulo;

    public PostulacionDTO(Long idPostulacion, String estudianteNombre, String estudianteEmail, String asignaturaNombre, String estadoPostulacion, Set<PostulacionDocumentoDTO> documentos, String convocatoriaTitulo) {
        this.idPostulacion = idPostulacion;
        this.estudianteNombre = estudianteNombre;
        this.estudianteEmail = estudianteEmail;
        this.asignaturaNombre = asignaturaNombre;
        this.estadoPostulacion = estadoPostulacion;
        this.documentos = documentos;
        this.convocatoriaTitulo = convocatoriaTitulo;
    }

    // Getters and setters

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public void setEstudianteNombre(String estudianteNombre) {
        this.estudianteNombre = estudianteNombre;
    }

    public String getEstudianteEmail() {
        return estudianteEmail;
    }

    public void setEstudianteEmail(String estudianteEmail) {
        this.estudianteEmail = estudianteEmail;
    }

    public String getAsignaturaNombre() {
        return asignaturaNombre;
    }

    public void setAsignaturaNombre(String asignaturaNombre) {
        this.asignaturaNombre = asignaturaNombre;
    }

    public String getEstadoPostulacion() {
        return estadoPostulacion;
    }

    public void setEstadoPostulacion(String estadoPostulacion) {
        this.estadoPostulacion = estadoPostulacion;
    }

    public Set<PostulacionDocumentoDTO> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<PostulacionDocumentoDTO> documentos) {
        this.documentos = documentos;
    }

    public String getConvocatoriaTitulo() {
        return convocatoriaTitulo;
    }

    public void setConvocatoriaTitulo(String convocatoriaTitulo) {
        this.convocatoriaTitulo = convocatoriaTitulo;
    }
}