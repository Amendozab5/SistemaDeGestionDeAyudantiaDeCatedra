package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "aud_prueba_oposicion")
public class AudPruebaOposicion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_prueba_oposicion_generator")
    @SequenceGenerator(name = "aud_prueba_oposicion_generator", sequenceName = "aud_prueba_oposicion_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_oposicion")
    private Long idOposicion;

    @Column(length = 255)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "fecha_oposicion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaOposicion;

    @Column(name = "puntaje_total", precision = 6, scale = 2)
    private BigDecimal puntajeTotal;

    @Column(length = 50)
    private String estado;

    @Column(length = 50)
    private String resultado;

    @Column(name = "fecha_horareg", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHorareg;

    @Column(length = 50)
    private String usuario;

    @Column(length = 1)
    private String tipo;

    // Getters and Setters

    public Long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(Long secuencia) {
        this.secuencia = secuencia;
    }

    public Long getIdOposicion() {
        return idOposicion;
    }

    public void setIdOposicion(Long idOposicion) {
        this.idOposicion = idOposicion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public Date getFechaOposicion() {
        return fechaOposicion;
    }

    public void setFechaOposicion(Date fechaOposicion) {
        this.fechaOposicion = fechaOposicion;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Date getFechaHorareg() {
        return fechaHorareg;
    }

    public void setFechaHorareg(Date fechaHorareg) {
        this.fechaHorareg = fechaHorareg;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
