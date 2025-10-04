package com.uteq.sgaac.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AgendarPruebaDTO {
    private Long postulacionId;
    private String tema;
    private String descripcion;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fechaHora;
    private String lugar;
}
