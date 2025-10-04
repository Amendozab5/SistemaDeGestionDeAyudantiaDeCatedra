package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ayudante_catedra", schema = "public")
public class AyudanteCatedra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ayudante")
    private Long idAyudante;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_postulacion", nullable = false, unique = true)
    private Postulacion postulacion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AyudantiaEstado estado = AyudantiaEstado.VIGENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private usuario_sistema usuario;

    // --- Getters y Setters ---

    public Long getIdAyudante() {
        return idAyudante;
    }

    public void setIdAyudante(Long idAyudante) {
        this.idAyudante = idAyudante;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public AyudantiaEstado getEstado() {
        return estado;
    }

    public void setEstado(AyudantiaEstado estado) {
        this.estado = estado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public usuario_sistema getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario_sistema usuario) {
        this.usuario = usuario;
    }
}