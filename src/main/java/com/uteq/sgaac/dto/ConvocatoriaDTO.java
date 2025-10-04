package com.uteq.sgaac.dto;

import java.time.LocalDate;
import java.util.List;

public class ConvocatoriaDTO {
    private Long idConvocatoria;
    private String titulo;
    private String descripcion; // Added field
    private String periodo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean estado;
    private List<ConvocatoriaAsignaturaDTO> convocatoriaAsignaturas;
    private int totalCupos;

    // Constructors
    public ConvocatoriaDTO() {
    }

    public ConvocatoriaDTO(Long idConvocatoria, String titulo, String descripcion, String periodo, LocalDate fechaInicio, LocalDate fechaFin, boolean estado, List<ConvocatoriaAsignaturaDTO> convocatoriaAsignaturas) {
        this.idConvocatoria = idConvocatoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.convocatoriaAsignaturas = convocatoriaAsignaturas;
        this.totalCupos = convocatoriaAsignaturas.stream().mapToInt(ConvocatoriaAsignaturaDTO::getCupos).sum();
    }

    // Getters and Setters
    public Long getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(Long idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
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

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<ConvocatoriaAsignaturaDTO> getConvocatoriaAsignaturas() {
        return convocatoriaAsignaturas;
    }

    public void setConvocatoriaAsignaturas(List<ConvocatoriaAsignaturaDTO> convocatoriaAsignaturas) {
        this.convocatoriaAsignaturas = convocatoriaAsignaturas;
    }

    public int getTotalCupos() {
        return totalCupos;
    }

    public void setTotalCupos(int totalCupos) {
        this.totalCupos = totalCupos;
    }
}
