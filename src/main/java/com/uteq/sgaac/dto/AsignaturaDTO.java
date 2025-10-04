package com.uteq.sgaac.dto;

public class AsignaturaDTO {
    private Long idAsignatura;
    private String nombre;

    // Constructors
    public AsignaturaDTO() {
    }

    public AsignaturaDTO(String nombre) {
        this.nombre = nombre;
    }

    public AsignaturaDTO(Long idAsignatura, String nombre) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
    }

    // Getters and Setters
    public Long getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(Long idAsignatura) {
        this.idAsignatura = idAsignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}