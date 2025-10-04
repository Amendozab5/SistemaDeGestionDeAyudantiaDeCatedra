package com.uteq.sgaac.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResultadoId implements Serializable {

    @Column(name = "id_postulacion")
    private Long postulacionId;

    public ResultadoId() {
    }

    public ResultadoId(Long postulacionId) {
        this.postulacionId = postulacionId;
    }

    // Getters, Setters, equals, and hashCode

    public Long getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(Long postulacionId) {
        this.postulacionId = postulacionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultadoId that = (ResultadoId) o;
        return Objects.equals(postulacionId, that.postulacionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postulacionId);
    }
}
