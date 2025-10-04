package com.uteq.sgaac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "postulacion_documento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"postulacion_id", "tipo_documento"})
})
public class PostulacionDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulacion_id", nullable = false)
    @JsonIgnore
    private Postulacion postulacion;

    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;

    @Column(name = "es_valido", nullable = false)
    private boolean esValido = false;

    @Column(name = "ruta_archivo", length = 255)
    private String rutaArchivo;

    @Column(name = "puntaje_asignado")
    private java.math.BigDecimal puntajeAsignado;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requisito", nullable = false)
    private Requisito requisito;

    // Constructors
    public PostulacionDocumento() {
    }

    public PostulacionDocumento(Postulacion postulacion, String tipoDocumento, boolean esValido, java.math.BigDecimal puntajeAsignado, String observaciones) {
        this.postulacion = postulacion;
        this.tipoDocumento = tipoDocumento;
        this.esValido = esValido;
        this.puntajeAsignado = puntajeAsignado;
        this.observaciones = observaciones;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
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

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
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