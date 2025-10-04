package com.uteq.sgaac.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class AgendarPruebaGrupoDTO {

    private String tema;
    private String descripcion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHora;

    private String lugar;
    private List<Long> tribunalIds;
    private List<Long> postulacionIds;

    // Getters and Setters
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public List<Long> getTribunalIds() {
        return tribunalIds;
    }

    public void setTribunalIds(List<Long> tribunalIds) {
        this.tribunalIds = tribunalIds;
    }

    public List<Long> getPostulacionIds() {
        return postulacionIds;
    }

    public void setPostulacionIds(List<Long> postulacionIds) {
        this.postulacionIds = postulacionIds;
    }
}
