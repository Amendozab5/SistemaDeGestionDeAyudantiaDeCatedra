package com.uteq.sgaac.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "semestre")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_semestre")
    private Integer idSemestre;

    @Column(length = 100)
    private String descripcion;

    public Semestre() {
    }

    public Semestre(Integer idSemestre, String descripcion) {
        this.idSemestre = idSemestre;
        this.descripcion = descripcion;
    }

    public Integer getIdSemestre() {
        return idSemestre;
    }

    public void setIdSemestre(Integer idSemestre) {
        this.idSemestre = idSemestre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}