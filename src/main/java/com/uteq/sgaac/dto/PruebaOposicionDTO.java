package com.uteq.sgaac.dto;

// Data Transfer Object para PruebaOposicion
// Un objeto simple para pasar datos a la vista de forma segura.
public class PruebaOposicionDTO {

    private String titulo;
    private String descripcion;
    private String fechaOposicionFormateada;
    private String estado;

    public PruebaOposicionDTO(String titulo, String descripcion, String fechaOposicionFormateada, String estado) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaOposicionFormateada = fechaOposicionFormateada;
        this.estado = estado;
    }

    // Getters y Setters

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

    public String getFechaOposicionFormateada() {
        return fechaOposicionFormateada;
    }

    public void setFechaOposicionFormateada(String fechaOposicionFormateada) {
        this.fechaOposicionFormateada = fechaOposicionFormateada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
