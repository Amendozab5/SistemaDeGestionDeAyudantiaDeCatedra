package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "datos_estudiantil")
public class DatosEstudiantil {

    @EmbeddedId
    private DatosEstudiantilId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("estudiante")
    @JoinColumn(name = "id_estudiante")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("asignatura")
    @JoinColumn(name = "id_asignatura")
    private Asignatura asignatura;

    @Column(name = "calificaciones", precision = 4, scale = 2)
    private BigDecimal calificaciones;

    @Column(name = "periodo", length = 30)
    private String periodo;

    // Getters and Setters

    public DatosEstudiantilId getId() {
        return id;
    }

    public void setId(DatosEstudiantilId id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public BigDecimal getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(BigDecimal calificaciones) {
        this.calificaciones = calificaciones;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
