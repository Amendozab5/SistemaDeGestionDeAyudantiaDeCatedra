package com.uteq.sgaac.dto;

import java.util.List;

public class PlazaFormDto {

    private Long convocatoriaId;
    private List<Long> requisitoIds;
    private List<AsignaturaCupoDto> asignaturas;

    // Getters y Setters
    public Long getConvocatoriaId() {
        return convocatoriaId;
    }

    public void setConvocatoriaId(Long convocatoriaId) {
        this.convocatoriaId = convocatoriaId;
    }

    public List<Long> getRequisitoIds() {
        return requisitoIds;
    }

    public void setRequisitoIds(List<Long> requisitoIds) {
        this.requisitoIds = requisitoIds;
    }

    public List<AsignaturaCupoDto> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<AsignaturaCupoDto> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
