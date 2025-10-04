package com.uteq.sgaac.model;

import java.io.Serializable;
import java.util.Objects;

public class RequisitoCumplidoId implements Serializable {

    private Long postulacion;
    private Long requisito;

    public RequisitoCumplidoId() {}

    public RequisitoCumplidoId(Long postulacion, Long requisito) {
        this.postulacion = postulacion;
        this.requisito = requisito;
    }

    // --- equals y hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequisitoCumplidoId that = (RequisitoCumplidoId) o;
        return Objects.equals(postulacion, that.postulacion) &&
               Objects.equals(requisito, that.requisito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postulacion, requisito);
    }
}
