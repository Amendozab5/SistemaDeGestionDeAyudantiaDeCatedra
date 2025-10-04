package com.uteq.sgaac.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "convocatoria_asignatura")
public class ConvocatoriaAsignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conv_asig")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_convocatoria", nullable = false)
    @JsonBackReference("convocatoria-convocatoriaAsignatura")
    private Convocatoria convocatoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignatura", nullable = false)
    @JsonBackReference("asignatura-convocatoria")
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordinador")
    private Coordinador coordinador;

    @Column(name = "cupos", nullable = false)
    private Integer cupos;

    @Column(name = "aprobado_decano", nullable = false)
    private boolean aprobadoDecano = false;

    @Column(name = "fecha_inicio_postulacion")
    private LocalDate fechaInicioPostulacion;

    @Column(name = "fecha_fin_postulacion")
    private LocalDate fechaFinPostulacion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "convocatoria_asignatura_requisito",
        joinColumns = @JoinColumn(name = "id_conv_asig"),
        inverseJoinColumns = @JoinColumn(name = "id_requisito")
    )
    private Set<Requisito> requisitos = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    public Integer getCupos() {
        return cupos;
    }

    public void setCupos(Integer cupos) {
        this.cupos = cupos;
    }

    public boolean isAprobadoDecano() {
        return aprobadoDecano;
    }

    public void setAprobadoDecano(boolean aprobadoDecano) {
        this.aprobadoDecano = aprobadoDecano;
    }

    public LocalDate getFechaInicioPostulacion() {
        return fechaInicioPostulacion;
    }

    public void setFechaInicioPostulacion(LocalDate fechaInicioPostulacion) {
        this.fechaInicioPostulacion = fechaInicioPostulacion;
    }

    public LocalDate getFechaFinPostulacion() {
        return fechaFinPostulacion;
    }

    public void setFechaFinPostulacion(LocalDate fechaFinPostulacion) {
        this.fechaFinPostulacion = fechaFinPostulacion;
    }

    public Set<Requisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(Set<Requisito> requisitos) {
        this.requisitos = requisitos;
    }
}