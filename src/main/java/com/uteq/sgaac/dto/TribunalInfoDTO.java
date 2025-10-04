package com.uteq.sgaac.dto;

import com.uteq.sgaac.model.PruebaOposicion;

public class TribunalInfoDTO {

    private PruebaOposicion pruebaOposicion;
    private String postulanteNombre;
    private String asignaturaNombre;

    public TribunalInfoDTO(PruebaOposicion pruebaOposicion, String postulanteNombre, String asignaturaNombre) {
        this.pruebaOposicion = pruebaOposicion;
        this.postulanteNombre = postulanteNombre;
        this.asignaturaNombre = asignaturaNombre;
    }

    public PruebaOposicion getPruebaOposicion() {
        return pruebaOposicion;
    }

    public void setPruebaOposicion(PruebaOposicion pruebaOposicion) {
        this.pruebaOposicion = pruebaOposicion;
    }

    public String getPostulanteNombre() {
        return postulanteNombre;
    }

    public void setPostulanteNombre(String postulanteNombre) {
        this.postulanteNombre = postulanteNombre;
    }

    public String getAsignaturaNombre() {
        return asignaturaNombre;
    }

    public void setAsignaturaNombre(String asignaturaNombre) {
        this.asignaturaNombre = asignaturaNombre;
    }
}
