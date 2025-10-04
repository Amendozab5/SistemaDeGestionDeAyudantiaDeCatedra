package com.uteq.sgaac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentoMeritoDTO {
    private Long id;
    private String rutaArchivo;
    private CriterioMeritoDTO criterioMerito;
}
