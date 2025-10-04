package com.uteq.sgaac.model;

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

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "estudiante", schema = "public")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante", nullable = false)
    private Long idEstudiante;

    // Relación con Carrera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false,
                foreignKey = @ForeignKey(name = "fk_estudiante_carrera"))
    @JsonIgnore
    private Carrera carrera;

    // Relación con UsuarioSistema
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false,
                foreignKey = @ForeignKey(name = "fk_estudiante_usuario"))
    private usuario_sistema usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad", nullable = false,
                foreignKey = @ForeignKey(name = "fk_est_fac"))
    @JsonIgnore
    private Facultad facultad;

    @Column(name = "es_regular", nullable = false)
    private boolean esRegular = true;

    @Column(name = "promedio_general")
    private BigDecimal promedioGeneral;

    // --- Getters y Setters ---

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public usuario_sistema getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario_sistema usuario) {
        this.usuario = usuario;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    public boolean isEsRegular() {
        return esRegular;
    }

    public void setEsRegular(boolean esRegular) {
        this.esRegular = esRegular;
    }

    public BigDecimal getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(BigDecimal promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }
}
