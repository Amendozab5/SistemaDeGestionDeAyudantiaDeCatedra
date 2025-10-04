package com.uteq.sgaac.model;

import jakarta.persistence.*;

@Entity
@Table(name = "convocatoria_asignatura_requisito")
public class ConvocatoriaAsignaturaRequisito {

    @EmbeddedId
    private ConvocatoriaAsignaturaRequisitoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("convocatoriaAsignaturaId")
    @JoinColumn(name = "id_conv_asig")
    private ConvocatoriaAsignatura convocatoriaAsignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requisitoId")
    @JoinColumn(name = "id_requisito")
    private Requisito requisito;

    public ConvocatoriaAsignaturaRequisito() {}

    public ConvocatoriaAsignaturaRequisito(ConvocatoriaAsignatura convocatoriaAsignatura, Requisito requisito) {
        this.convocatoriaAsignatura = convocatoriaAsignatura;
        this.requisito = requisito;
        this.id = new ConvocatoriaAsignaturaRequisitoId(convocatoriaAsignatura.getId(), requisito.getId());
    }

    // Getters and Setters

    public ConvocatoriaAsignaturaRequisitoId getId() {
        return id;
    }

    public void setId(ConvocatoriaAsignaturaRequisitoId id) {
        this.id = id;
    }

    public ConvocatoriaAsignatura getConvocatoriaAsignatura() {
        return convocatoriaAsignatura;
    }

    public void setConvocatoriaAsignatura(ConvocatoriaAsignatura convocatoriaAsignatura) {
        this.convocatoriaAsignatura = convocatoriaAsignatura;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }
}
