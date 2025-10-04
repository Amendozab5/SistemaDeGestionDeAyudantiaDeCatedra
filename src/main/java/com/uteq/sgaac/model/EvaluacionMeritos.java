package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "evaluacion_meritos", schema = "public")
public class EvaluacionMeritos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion", nullable = false)
    private Long idEvaluacion;

    // Relación con Postulacion (única)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_postulacion", nullable = false,
                unique = true,
                foreignKey = @ForeignKey(name = "fk_meritos_post"))
    private Postulacion postulacion;

    @Column(name = "puntaje_total", nullable = false, precision = 6, scale = 2)
    private BigDecimal puntajeTotal;

    @Column(name = "estado", nullable = false)
    private String estado = "NO_CALCULADO";

    // Desglose de puntos
    @Column(name = "puntos_calificacion_asignatura", precision = 6, scale = 2)
    private BigDecimal puntosCalificacionAsignatura;

    @Column(name = "puntos_promedio_general", precision = 6, scale = 2)
    private BigDecimal puntosPromedioGeneral;

    @Column(name = "puntos_experiencia", precision = 6, scale = 2)
    private BigDecimal puntosExperiencia;

    @Column(name = "puntos_eventos", precision = 6, scale = 2)
    private BigDecimal puntosEventos;


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


    public Long getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
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
}
