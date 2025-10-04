package com.uteq.sgaac.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "postulacion", schema = "public")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false, foreignKey = @ForeignKey(name = "fk_post_est"))
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignatura", nullable = false, foreignKey = @ForeignKey(name = "fk_post_asig"))
    private Asignatura asignatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_postulacion", nullable = false)
    private PostulacionEstado estadoPostulacion = PostulacionEstado.EN_REVISION;

    @Column(name = "fecha_postulacion")
    private LocalDateTime fechaPostulacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_convocatoria", nullable = false, foreignKey = @ForeignKey(name = "fk_postulacion_convocatoria"))
    private Convocatoria convocatoria;

    @OneToMany(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostulacionRequisito> requisitos;

    @OneToOne(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Resultado resultado;

    // Getters and Setters

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Long getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(Long idPostulacion) {
        this.idPostulacion = idPostulacion;
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

    public PostulacionEstado getEstadoPostulacion() {
        return estadoPostulacion;
    }

    public void setEstadoPostulacion(PostulacionEstado estadoPostulacion) {
        this.estadoPostulacion = estadoPostulacion;
    }

    public LocalDateTime getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDateTime fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    public Set<PostulacionRequisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(Set<PostulacionRequisito> requisitos) {
        this.requisitos = requisitos;
    }


    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    @OneToMany(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostulacionDocumento> documentos = new HashSet<>();

    @OneToMany(mappedBy = "postulacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentoMerito> documentosMerito = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "postulacion_tribunal",
        joinColumns = @JoinColumn(name = "id_postulacion"),
        inverseJoinColumns = @JoinColumn(name = "id_docente")
    )
    private Set<Docente> tribunalAsignado = new HashSet<>();

    public void addDocumento(PostulacionDocumento documento) {
        documentos.add(documento);
        documento.setPostulacion(this);
    }

    public Set<PostulacionDocumento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<PostulacionDocumento> documentos) {
        this.documentos = documentos;
    }

    public Set<DocumentoMerito> getDocumentosMerito() {
        return documentosMerito;
    }

    public void setDocumentosMerito(Set<DocumentoMerito> documentosMerito) {
        this.documentosMerito = documentosMerito;
    }

    public Set<Docente> getTribunalAsignado() {
        return tribunalAsignado;
    }

    public void setTribunalAsignado(Set<Docente> tribunalAsignado) {
        this.tribunalAsignado = tribunalAsignado;
    }
}