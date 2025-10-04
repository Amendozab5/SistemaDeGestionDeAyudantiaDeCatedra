package com.uteq.sgaac.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "docente")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asignatura", nullable = true)
    @JsonBackReference("asignatura-docente")
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_facultad")
    private Facultad facultad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private usuario_sistema usuario;

    @JsonIgnore
    @ManyToMany(mappedBy = "tribunal")
    private Set<PruebaOposicion> pruebasAsignadas = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    public usuario_sistema getUsuario() {
        return usuario;
    }

    public void setUsuario(usuario_sistema usuario) {
        this.usuario = usuario;
    }

    public Set<PruebaOposicion> getPruebasAsignadas() {
        return pruebasAsignadas;
    }

    public void setPruebasAsignadas(Set<PruebaOposicion> pruebasAsignadas) {
        this.pruebasAsignadas = pruebasAsignadas;
    }
}