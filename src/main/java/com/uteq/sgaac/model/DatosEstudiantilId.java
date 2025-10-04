package com.uteq.sgaac.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DatosEstudiantilId implements Serializable {

    private Long estudiante;
    private Long asignatura;

    // Getters, Setters, equals, and hashCode

    public Long getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Long estudiante) {
        this.estudiante = estudiante;
    }

    public Long getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Long asignatura) {
        this.asignatura = asignatura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatosEstudiantilId that = (DatosEstudiantilId) o;
        return Objects.equals(estudiante, that.estudiante) &&
               Objects.equals(asignatura, that.asignatura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudiante, asignatura);
    }
}
