package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_ayudante", schema = "public")
public class ActividadAyudante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_actividad", nullable = false)
    private Long idActividad;

    // Relación con AyudanteCatedra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ayudante", nullable = false,
                foreignKey = @ForeignKey(name = "fk_act_ayudante"))
    private AyudanteCatedra ayudante;

    // Relación con Asignatura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignatura", nullable = false,
                foreignKey = @ForeignKey(name = "fk_act_asignatura"))
    private Asignatura asignatura;

    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;

    @Column(name = "tipo_actividad", length = 60)
    private String tipoActividad;

    @Column(name = "fecha_actividad", nullable = false)
    private LocalDateTime fechaActividad = LocalDateTime.now();

    @Column(name = "asistencia")
    private Boolean asistencia;

    @Column(name = "observaciones", columnDefinition = "text")
    private String observaciones;

    // --- Getters y Setters ---

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    public AyudanteCatedra getAyudante() {
        return ayudante;
    }

    public void setAyudante(AyudanteCatedra ayudante) {
        this.ayudante = ayudante;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public LocalDateTime getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(LocalDateTime fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

    public Boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
