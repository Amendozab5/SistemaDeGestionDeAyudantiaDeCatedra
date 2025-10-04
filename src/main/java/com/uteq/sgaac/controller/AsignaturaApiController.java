package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.AsignaturaDTO;
import com.uteq.sgaac.dto.AsignaturaDetailDTO;
import com.uteq.sgaac.dto.AsignaturaUpdateDTO;
import com.uteq.sgaac.dto.DocenteDTO;
import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Semestre;
import com.uteq.sgaac.repository.AsignaturaRepository;
import com.uteq.sgaac.repository.CarreraRepository;
import com.uteq.sgaac.repository.SemestreRepository;
import com.uteq.sgaac.services.AsignaturaService;
import com.uteq.sgaac.services.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AsignaturaApiController {

    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private DocenteService docenteService;
    @Autowired
    private CarreraRepository carreraRepository;
    @Autowired
    private SemestreRepository semestreRepository;
    @Autowired
    private AsignaturaService asignaturaService;


    @GetMapping("/asignaturas/semestre")
    public List<AsignaturaDTO> getAsignaturasPorSemestre(@RequestParam Integer semestre) {
        List<Asignatura> asignaturas = asignaturaRepository.findBySemestre_IdSemestre(semestre);
        return asignaturas.stream()
                .map(asignatura -> new AsignaturaDTO(asignatura.getIdAsignatura(), asignatura.getNombre()))
                .collect(Collectors.toList());
    }

    @GetMapping("/asignaturas/{id}")
    public ResponseEntity<AsignaturaDetailDTO> getAsignaturaById(@PathVariable Long id) {
        try {
            AsignaturaDetailDTO asignaturaDTO = asignaturaService.findDetailById(id);
            return ResponseEntity.ok(asignaturaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/asignaturas/{id}/docentes")
    public ResponseEntity<List<DocenteDTO>> getDocentesByAsignatura(@PathVariable Long id) {
        List<DocenteDTO> docentes = docenteService.findDocentesByAsignatura(id);
        return ResponseEntity.ok(docentes);
    }

    @GetMapping("/docentes")
    public List<DocenteDTO> getAllDocentes() {
        return docenteService.findAllDocentesAsDTO();
    }

    @GetMapping("/carreras")
    public List<Carrera> getAllCarreras() {
        return carreraRepository.findAll();
    }

    @GetMapping("/semestres")
    public List<Semestre> getAllSemestres() {
        return semestreRepository.findAll();
    }

    @PutMapping("/asignaturas/{id}")
    public ResponseEntity<AsignaturaDetailDTO> updateAsignatura(@PathVariable Long id, @RequestBody AsignaturaUpdateDTO asignaturaDetailsDTO) {
        try {
            AsignaturaDetailDTO updatedAsignatura = asignaturaService.update(id, asignaturaDetailsDTO);
            if (updatedAsignatura != null) {
                return ResponseEntity.ok(updatedAsignatura);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/asignaturas/{id}")
    public ResponseEntity<Void> deleteAsignatura(@PathVariable Long id) {
        try {
            boolean deleted = asignaturaService.delete(id);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
