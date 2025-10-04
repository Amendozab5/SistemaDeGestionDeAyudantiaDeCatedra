package com.uteq.sgaac.dto;

import lombok.Data;

import java.util.List;

@Data
public class AsistenciaForm {
    private Long idAsignatura;
    private String fechaSesion;
    private List<AsistenciaDTO> asistencias;
}
