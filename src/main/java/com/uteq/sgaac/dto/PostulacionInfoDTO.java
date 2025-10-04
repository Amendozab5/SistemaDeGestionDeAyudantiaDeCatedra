package com.uteq.sgaac.dto;

import com.uteq.sgaac.model.Postulacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostulacionInfoDTO {
    private Postulacion postulacion;
    private String estudianteNombre;
    private String asignaturaNombre;
    private BigDecimal calificacion;
}
