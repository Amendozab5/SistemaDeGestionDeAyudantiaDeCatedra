package com.uteq.sgaac.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PostulacionRequisitoId implements Serializable {

    @Column(name = "id_postulacion")
    private Long postulacionId;

    @Column(name = "id_requisito")
    private Long requisitoId;

    public PostulacionRequisitoId() {
    }

    public PostulacionRequisitoId(Long postulacionId, Long requisitoId) {
        this.postulacionId = postulacionId;
        this.requisitoId = requisitoId;
    }

    // Getters, Setters, equals, and hashCode

    public Long getPostulacionId() {
        return postulacionId;
    }

    public void setPostulacionId(Long postulacionId) {
        this.postulacionId = postulacionId;
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
        PostulacionRequisitoId that = (PostulacionRequisitoId) o;
        return Objects.equals(postulacionId, that.postulacionId) &&
               Objects.equals(requisitoId, that.requisitoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postulacionId, requisitoId);
    }
}
