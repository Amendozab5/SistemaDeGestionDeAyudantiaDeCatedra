package com.uteq.sgaac.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConvocatoriaAsignaturaRequisitoId implements Serializable {

    @Column(name = "id_conv_asig")
    private Long convocatoriaAsignaturaId;

    @Column(name = "id_requisito")
    private Long requisitoId;

    public ConvocatoriaAsignaturaRequisitoId() {}

    public ConvocatoriaAsignaturaRequisitoId(Long convocatoriaAsignaturaId, Long requisitoId) {
        this.convocatoriaAsignaturaId = convocatoriaAsignaturaId;
        this.requisitoId = requisitoId;
    }

    // Getters, Setters, equals, and hashCode

    public Long getConvocatoriaAsignaturaId() {
        return convocatoriaAsignaturaId;
    }

    public void setConvocatoriaAsignaturaId(Long convocatoriaAsignaturaId) {
        this.convocatoriaAsignaturaId = convocatoriaAsignaturaId;
    }

    public Long getRequisitoId() {
        return requisitoId;
    }

    public void setRequisitoId(Long requisitoId) {
        this.requisitoId = requisitoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvocatoriaAsignaturaRequisitoId that = (ConvocatoriaAsignaturaRequisitoId) o;
        return Objects.equals(convocatoriaAsignaturaId, that.convocatoriaAsignaturaId) &&
               Objects.equals(requisitoId, that.requisitoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(convocatoriaAsignaturaId, requisitoId);
    }
}
