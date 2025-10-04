package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.RequisitoDTO;
import com.uteq.sgaac.services.RequisitoService;
import com.uteq.sgaac.services.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requisitos")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class RequisitoApiController {

    private final RequisitoService requisitoService;
    private final StorageService storageService;

    public RequisitoApiController(RequisitoService requisitoService, StorageService storageService) {
        this.requisitoService = requisitoService;
        this.storageService = storageService;
    }

    @GetMapping
    public List<RequisitoDTO> getAll() {
        return requisitoService.findAll();
    }

    @PostMapping
    public RequisitoDTO create(@RequestPart("requisito") RequisitoDTO dto,
                               @RequestPart(name = "archivo", required = false) MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            String filename = storageService.store(archivo);
            dto.setUrlPlantilla("/uploads/" + filename); // URL para acceder al archivo
        }
        dto.setId(null); // Ensure it's a new entity
        return requisitoService.save(dto);
    }

    @PutMapping("/{id}")
    public RequisitoDTO update(@PathVariable Long id,
                               @RequestPart("requisito") RequisitoDTO dto,
                               @RequestPart(name = "archivo", required = false) MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            String filename = storageService.store(archivo);
            dto.setUrlPlantilla("/uploads/" + filename);
        }
        return requisitoService.update(id, dto);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> toggleRequisitoStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean activo = body.get("activo");
        if (activo != null) {
            requisitoService.updateStatus(id, activo);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        requisitoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
