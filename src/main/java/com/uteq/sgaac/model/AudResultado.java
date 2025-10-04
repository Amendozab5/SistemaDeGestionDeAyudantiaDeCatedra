package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "aud_resultado")
public class AudResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_resultado_generator")
    @SequenceGenerator(name = "aud_resultado_generator", sequenceName = "aud_resultado_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "estado_final", length = 255)
    private String estadoFinal;

    @Column(name = "puntaje_total_final", precision = 6, scale = 2)
    private BigDecimal puntajeTotalFinal;

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

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public String getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(String estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public BigDecimal getPuntajeTotalFinal() {
        return puntajeTotalFinal;
    }

    public void setPuntajeTotalFinal(BigDecimal puntajeTotalFinal) {
        this.puntajeTotalFinal = puntajeTotalFinal;
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
