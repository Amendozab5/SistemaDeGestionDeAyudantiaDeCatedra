package com.uteq.sgaac.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "prueba_oposicion_eval", schema = "public",
       uniqueConstraints = {
           @UniqueConstraint(name = "prueba_op_eval_unique", columnNames = {"id_oposicion", "id_docente"})
       })
public class PruebaOposicionEval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_eval", nullable = false)
    private Long idEval;

    // Relación con PruebaOposicion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oposicion", nullable = false,
                foreignKey = @ForeignKey(name = "fk_eval_oposicion"))
    private PruebaOposicion oposicion;

    // Relación con Docente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false,
                foreignKey = @ForeignKey(name = "fk_eval_docente"))
    private Docente docente;

    @Column(name = "material_puntos", nullable = false, precision = 4, scale = 2)
    private BigDecimal materialPuntos;

    @Column(name = "calidad_puntos", nullable = false, precision = 4, scale = 2)
    private BigDecimal calidadPuntos;

    @Column(name = "respuestas_puntos", nullable = false, precision = 4, scale = 2)
    private BigDecimal respuestasPuntos;

    @Column(name = "puntaje_total", precision = 5, scale = 2)
    private BigDecimal puntajeTotal;

    @Column(name = "comentarios", columnDefinition = "text")
    private String comentarios;

    // --- Getters y Setters ---

    public Long getIdEval() {
        return idEval;
    }

    public void setIdEval(Long idEval) {
        this.idEval = idEval;
    }

    public PruebaOposicion getOposicion() {
        return oposicion;
    }

    public void setOposicion(PruebaOposicion oposicion) {
        this.oposicion = oposicion;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public BigDecimal getMaterialPuntos() {
        return materialPuntos;
    }

    public void setMaterialPuntos(BigDecimal materialPuntos) {
        this.materialPuntos = materialPuntos;
    }

    public BigDecimal getCalidadPuntos() {
        return calidadPuntos;
    }

    public void setCalidadPuntos(BigDecimal calidadPuntos) {
        this.calidadPuntos = calidadPuntos;
    }

    public BigDecimal getRespuestasPuntos() {
        return respuestasPuntos;
    }

    public void setRespuestasPuntos(BigDecimal respuestasPuntos) {
        this.respuestasPuntos = respuestasPuntos;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
