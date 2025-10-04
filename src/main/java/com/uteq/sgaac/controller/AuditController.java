package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.*;
import com.uteq.sgaac.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/actividad-ayudante")
    public List<AudActividadAyudante> getActividadAyudanteAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findActividadAyudanteAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/ayudante-catedra")
    public List<AudAyudanteCatedra> getAyudanteCatedraAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findAyudanteCatedraAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/evaluacion-meritos")
    public List<AudEvaluacionMeritos> getEvaluacionMeritosAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findEvaluacionMeritosAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/postulaciones")
    public List<AudPostulacion> getPostulacionAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findPostulacionAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/prueba-oposicion")
    public List<AudPruebaOposicion> getPruebaOposicionAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findPruebaOposicionAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/resultados")
    public List<AudResultado> getResultadoAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findResultadoAudit(startDate, endDate, usuario, tipo);
    }

    @GetMapping("/roles")
    public List<AudRoles> getRolesAudit(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String tipo) {
        return auditService.findRolesAudit(startDate, endDate, usuario, tipo);
    }
}
