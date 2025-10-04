package com.uteq.sgaac.dto;

public class PostulacionDocumentoDTO {
    private Long id;
    private String tipoDocumento;
    private boolean esValido;
    private String rutaArchivo;
    private RequisitoDTO requisito;
    private java.math.BigDecimal puntajeAsignado;
    private String observaciones;

    public PostulacionDocumentoDTO(Long id, String tipoDocumento, boolean esValido, String rutaArchivo, RequisitoDTO requisito, java.math.BigDecimal puntajeAsignado, String observaciones) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.esValido = esValido;
        this.rutaArchivo = rutaArchivo;
        this.requisito = requisito;
        this.puntajeAsignado = puntajeAsignado;
        this.observaciones = observaciones;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public boolean isEsValido() {
        return esValido;
    }

    public void setEsValido(boolean esValido) {
        this.esValido = esValido;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public RequisitoDTO getRequisito() {
        return requisito;
    }

    public void setRequisito(RequisitoDTO requisito) {
        this.requisito = requisito;
    }

    public java.math.BigDecimal getPuntajeAsignado() {
        return puntajeAsignado;
    }

    public void setPuntajeAsignado(java.math.BigDecimal puntajeAsignado) {
        this.puntajeAsignado = puntajeAsignado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}