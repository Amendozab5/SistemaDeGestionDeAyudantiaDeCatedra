package com.uteq.sgaac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "prueba_oposicion")
public class PruebaOposicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oposicion")
    private Long idOposicion;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_postulacion", nullable = false, unique = true)
    private Postulacion postulacion;

    @Column(name = "titulo", length = 255)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_oposicion")
    private LocalDateTime fechaOposicion;

    @Column(name = "lugar", length = 255)
    private String lugar;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "puntaje_total", precision = 6, scale = 2)
    private BigDecimal puntajeTotal;

    @Column(name = "resultado", length = 50)
    private String resultado;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "prueba_oposicion_tribunal",
        joinColumns = @JoinColumn(name = "id_oposicion"),
        inverseJoinColumns = @JoinColumn(name = "id_docente")
    )
    private Set<Docente> tribunal = new HashSet<>();

    public PruebaOposicion() {
        this.estado = "AGENDADA";
    }

    // Getters y Setters

    public Long getIdOposicion() {
        return idOposicion;
    }

    public void setIdOposicion(Long idOposicion) {
        this.idOposicion = idOposicion;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaOposicion() {
        return fechaOposicion;
    }

    public void setFechaOposicion(LocalDateTime fechaOposicion) {
        this.fechaOposicion = fechaOposicion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(BigDecimal puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Set<Docente> getTribunal() {
        return tribunal;
    }

    public void setTribunal(Set<Docente> tribunal) {
        this.tribunal = tribunal;
    }
}