package com.uteq.sgaac.model;

import com.uteq.sgaac.model.enums.AyudantiaEstadoEnum;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aud_ayudante_catedra")
public class AudAyudanteCatedra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aud_ayudante_catedra_generator")
    @SequenceGenerator(name = "aud_ayudante_catedra_generator", sequenceName = "aud_ayudante_catedra_secuencia_seq", allocationSize = 1)
    private Long secuencia;

    @Column(name = "id_ayudante")
    private Long idAyudante;

    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ayudantia_estado_enum")
    private AyudantiaEstadoEnum estado;

    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @Column(name = "id_usuario")
    private Long idUsuario;

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

    public Long getIdAyudante() {
        return idAyudante;
    }

    public void setIdAyudante(Long idAyudante) {
        this.idAyudante = idAyudante;
    }

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public AyudantiaEstadoEnum getEstado() {
        return estado;
    }

    public void setEstado(AyudantiaEstadoEnum estado) {
        this.estado = estado;
    }

    public Integer getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Integer idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
