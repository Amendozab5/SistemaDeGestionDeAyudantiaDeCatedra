package com.uteq.sgaac.dto;

public class ConvocatoriaAsignaturaDTO {
    private int cupos;
    private AsignaturaDTO asignatura;

    // Constructors
    public ConvocatoriaAsignaturaDTO() {
    }

    public ConvocatoriaAsignaturaDTO(int cupos, AsignaturaDTO asignatura) {
        this.cupos = cupos;
        this.asignatura = asignatura;
    }

    // Getters and Setters
    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public AsignaturaDTO getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(AsignaturaDTO asignatura) {
        this.asignatura = asignatura;
    }
}
