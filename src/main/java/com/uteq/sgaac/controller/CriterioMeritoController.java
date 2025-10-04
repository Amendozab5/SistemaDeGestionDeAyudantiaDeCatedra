package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.CriterioMerito;
import com.uteq.sgaac.services.CriterioMeritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/criterios-merito")
@PreAuthorize("hasAuthority('ADMIN')")
public class CriterioMeritoController {

    @Autowired
    private CriterioMeritoService criterioMeritoService;

    @GetMapping
    public List<CriterioMerito> getAllCriterios() {
        return criterioMeritoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CriterioMerito> getCriterioById(@PathVariable Long id) {
        return criterioMeritoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CriterioMerito> createCriterio(@RequestBody CriterioMerito criterio) {
        CriterioMerito savedCriterio = criterioMeritoService.save(criterio);
        return ResponseEntity.created(URI.create("/api/criterios-merito/" + savedCriterio.getId()))
                .body(savedCriterio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriterioMerito> updateCriterio(@PathVariable Long id, @RequestBody CriterioMerito criterio) {
        return criterioMeritoService.findById(id)
                .map(existingCriterio -> {
                    criterio.setId(id);
                    return ResponseEntity.ok(criterioMeritoService.save(criterio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCriterio(@PathVariable Long id) {
        if (!criterioMeritoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        criterioMeritoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
