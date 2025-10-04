package com.uteq.sgaac.model;

import jakarta.persistence.*;

@Entity
@Table(name = "postulacion_requisito")
public class PostulacionRequisito {

    @EmbeddedId
    private PostulacionRequisitoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postulacionId")
    @JoinColumn(name = "id_postulacion")
    private Postulacion postulacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requisitoId")
    @JoinColumn(name = "id_requisito")
    private Requisito requisito;

    @Column(name = "cumplido")
    private boolean cumplido;

    // Getters and Setters

    public PostulacionRequisitoId getId() {
        return id;
    }

    public void setId(PostulacionRequisitoId id) {
        this.id = id;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public boolean isCumplido() {
        return cumplido;
    }

    public void setCumplido(boolean cumplido) {
        this.cumplido = cumplido;
    }
}
