package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Facultad;
import com.uteq.sgaac.services.AsignaturaService;
import com.uteq.sgaac.services.CarreraService;
import com.uteq.sgaac.services.FacultadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestionacademica")
public class GestionAcademicaApiController {

    @Autowired
    private FacultadService facultadService;

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private AsignaturaService asignaturaService;

    @GetMapping("/facultades")
    public List<Facultad> getFacultades() {
        return facultadService.findAll();
    }

    @GetMapping("/carreras")
    public List<Carrera> getCarreras(@RequestParam Long facultyId) {
        return carreraService.findByFacultad_Id(facultyId);
    }

    @GetMapping("/facultad/{id}")
    public Facultad getFacultad(@PathVariable Long id) {
        return facultadService.findById(id);
    }

    @GetMapping("/carrera/{id}")
    public Carrera getCarrera(@PathVariable Long id) {
        return carreraService.findById(id);
    }

    @GetMapping("/asignaturas")
    public ResponseEntity<List<Asignatura>> getAsignaturas(
            @RequestParam(required = false) Long carreraId,
            @RequestParam(required = false) Integer semestreId) {
        
        if (carreraId != null && semestreId != null) {
            return ResponseEntity.ok(asignaturaService.findByCarreraAndSemestre(carreraId, semestreId));
        }
        return ResponseEntity.ok(asignaturaService.findAll());
    }

}
