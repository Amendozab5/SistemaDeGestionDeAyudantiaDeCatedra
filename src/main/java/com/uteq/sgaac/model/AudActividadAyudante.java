package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aud_actividad_ayudante")
public class AudActividadAyudante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_actividad_ayudante_generator")
    @SequenceGenerator(name = "aud_actividad_ayudante_generator", sequenceName = "aud_actividad_ayudante_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_actividad")
    private Long idActividad;

    @Column(name = "id_ayudante")
    private Long idAyudante;

    @Column(name = "id_asignatura")
    private Long idAsignatura;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "tipo_actividad", length = 60)
    private String tipoActividad;

    @Column(name = "fecha_actividad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActividad;

    private boolean asistencia;

    @Column(columnDefinition = "text")
    private String observaciones;

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

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    public Long getIdAyudante() {
        return idAyudante;
    }

    public void setIdAyudante(Long idAyudante) {
        this.idAyudante = idAyudante;
    }

    public Long getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(Long idAsignatura) {
        this.idAsignatura = idAsignatura;
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

    public Date getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(Date fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
