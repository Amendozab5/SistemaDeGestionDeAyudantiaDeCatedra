package com.uteq.sgaac.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pruebas-oposicion")
public class PruebaOposicionController {


    // DTO (Data Transfer Object) para recibir los datos del formulario
    public static class AgendarPruebaRequest {
        private Long idPostulacion;
        private String titulo;
        private String descripcion;
        private LocalDateTime fechaOposicion;

        // Getters y Setters
        public Long getIdPostulacion() { return idPostulacion; }
        public void setIdPostulacion(Long idPostulacion) { this.idPostulacion = idPostulacion; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public LocalDateTime getFechaOposicion() { return fechaOposicion; }
        public void setFechaOposicion(LocalDateTime fechaOposicion) { this.fechaOposicion = fechaOposicion; }
    }

    /*@PostMapping("/agendar")
    public ResponseEntity<PruebaOposicion> agendarPrueba(@RequestBody AgendarPruebaRequest request) {
        try {
            PruebaOposicion nuevaPrueba = pruebaOposicionService.agendarPrueba(
                    request.getIdPostulacion(),
                    request.getTitulo(),
                    request.getDescripcion(),
                    request.getFechaOposicion()
            );
            return new ResponseEntity<>(nuevaPrueba, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the exception e
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}
