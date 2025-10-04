package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aud_postulacion")
public class AudPostulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_postulacion_generator")
    @SequenceGenerator(name = "aud_postulacion_generator", sequenceName = "aud_postulacion_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "id_estudiante")
    private Long idEstudiante;

    @Column(name = "id_asignatura")
    private Long idAsignatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_postulacion", length = 20)
    private PostulacionEstado estadoPostulacion;

    @Column(name = "id_convocatoria")
    private Long idConvocatoria;

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

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Long getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(Long idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public PostulacionEstado getEstadoPostulacion() {
        return estadoPostulacion;
    }

    public void setEstadoPostulacion(PostulacionEstado estadoPostulacion) {
        this.estadoPostulacion = estadoPostulacion;
    }

    public Long getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(Long idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
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
