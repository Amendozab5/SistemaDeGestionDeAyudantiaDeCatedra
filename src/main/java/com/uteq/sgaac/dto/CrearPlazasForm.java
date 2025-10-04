package com.uteq.sgaac.dto;

import java.util.List;

public class CrearPlazasForm {

    private Long convocatoriaId;
    private List<ConvocatoriaAsignaturaItemDTO> asignaturas;

    // Getters and Setters
    public Long getConvocatoriaId() {
        return convocatoriaId;
    }

    public void setConvocatoriaId(Long convocatoriaId) {
        this.convocatoriaId = convocatoriaId;
    }

    public List<ConvocatoriaAsignaturaItemDTO> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<ConvocatoriaAsignaturaItemDTO> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
