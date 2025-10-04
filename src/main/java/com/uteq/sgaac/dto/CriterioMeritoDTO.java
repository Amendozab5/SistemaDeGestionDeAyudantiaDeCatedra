package com.uteq.sgaac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriterioMeritoDTO {
    private Long id;
    private String descripcion;
    private double puntosMaximos;
}
