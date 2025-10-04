package com.uteq.sgaac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsignaturaDetailDTO {
    private Long idAsignatura;
    private String codigo;
    private String nombre;
    private SemestreDTO semestre;
    private CarreraDTO carrera;
    private DocenteDTO docente;
}
