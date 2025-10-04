package com.uteq.sgaac.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prueba_oposicion_tribunal")
public class PruebaOposicionTribunal {

    @EmbeddedId
    private PruebaOposicionTribunalId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOposicion")
    @JoinColumn(name = "id_oposicion")
    private PruebaOposicion pruebaOposicion;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDocente")
    @JoinColumn(name = "id_docente")
    private Docente docente;

    public PruebaOposicionTribunal() {
    }

    public PruebaOposicionTribunal(PruebaOposicion pruebaOposicion, Docente docente) {
        this.pruebaOposicion = pruebaOposicion;
        this.docente = docente;
        this.id = new PruebaOposicionTribunalId(pruebaOposicion.getIdOposicion(), docente.getId());
    }

    public PruebaOposicionTribunalId getId() {
        return id;
    }

    public void setId(PruebaOposicionTribunalId id) {
        this.id = id;
    }

    public PruebaOposicion getPruebaOposicion() {
        return pruebaOposicion;
    }

    public void setPruebaOposicion(PruebaOposicion pruebaOposicion) {
        this.pruebaOposicion = pruebaOposicion;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
