package com.uteq.sgaac.dto;

import com.uteq.sgaac.model.AsistenciaEstadoEnum;
import lombok.Data;

@Data
public class AsistenciaDTO {
    private Long idEstudiante;
    private AsistenciaEstadoEnum estado;
    private String observaciones;
}
