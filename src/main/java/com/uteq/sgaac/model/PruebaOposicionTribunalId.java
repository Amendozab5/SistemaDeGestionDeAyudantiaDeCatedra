package com.uteq.sgaac.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PruebaOposicionTribunalId implements Serializable {

    @Column(name = "id_oposicion")
    private Long idOposicion;

    @Column(name = "id_docente")
    private Long idDocente;

    public PruebaOposicionTribunalId() {
    }

    public PruebaOposicionTribunalId(Long idOposicion, Long idDocente) {
        this.idOposicion = idOposicion;
        this.idDocente = idDocente;
    }

    public Long getIdOposicion() {
        return idOposicion;
    }

    public void setIdOposicion(Long idOposicion) {
        this.idOposicion = idOposicion;
    }

    public Long getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Long idDocente) {
        this.idDocente = idDocente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PruebaOposicionTribunalId that = (PruebaOposicionTribunalId) o;
        return Objects.equals(idOposicion, that.idOposicion) &&
               Objects.equals(idDocente, that.idDocente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOposicion, idDocente);
    }
}
