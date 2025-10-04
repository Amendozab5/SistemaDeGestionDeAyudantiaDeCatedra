package com.uteq.sgaac.dto;

import lombok.Data;

@Data
public class AsignaturaUpdateDTO {
    private String codigo;
    private String nombre;
    private Integer id_semestre;
    private Long id_carrera;
    private Long id_docente;
}
