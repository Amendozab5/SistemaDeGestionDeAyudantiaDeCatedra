package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.Notificacion;
import com.uteq.sgaac.repository.UsuarioSistemaRepository;
import com.uteq.sgaac.services.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioSistemaRepository usuarioSistemaRepository;

    @Autowired
    public NotificacionController(NotificacionService notificacionService, UsuarioSistemaRepository usuarioSistemaRepository) {
        this.notificacionService = notificacionService;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notificacion>> getMisNotificaciones(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        return usuarioSistemaRepository.findByUsername(principal.getName())
                .map(usuario -> {
                    List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesDeUsuario(usuario);
                    return ResponseEntity.ok(notificaciones);
                })
                .orElse(ResponseEntity.status(404).build()); // Usuario no encontrado en la BD
    }
}