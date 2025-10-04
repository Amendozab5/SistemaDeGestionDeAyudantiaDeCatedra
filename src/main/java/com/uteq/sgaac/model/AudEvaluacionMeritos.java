package com.uteq.sgaac.model;

import com.uteq.sgaac.model.enums.MeritosEstadoEnum;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "aud_evaluacion_meritos")
public class AudEvaluacionMeritos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_evaluacion_meritos_generator")
    @SequenceGenerator(name = "aud_evaluacion_meritos_generator", sequenceName = "aud_evaluacion_meritos_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_evaluacion")
    private Long idEvaluacion;

    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "puntaje_total", precision = 6, scale = 2)
    private BigDecimal puntajeTotal;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "meritos_estado_enum")
    private MeritosEstadoEnum estado;

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

    public Long getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public MeritosEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(MeritosEstadoEnum estado) {
        this.estado = estado;
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
