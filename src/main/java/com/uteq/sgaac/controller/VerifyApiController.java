package com.uteq.sgaac.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uteq.sgaac.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class VerifyApiController {

    private final UsuarioRepository usuarioRepository;

    public VerifyApiController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verificarCuenta(@RequestParam("token") String token) {
        return usuarioRepository.findByTokenConfirmacion(token)
                .map(usuario -> {
                    usuario.setConfirmado(true);
                    usuario.setTokenConfirmacion(null);
                    usuarioRepository.save(usuario);

                    return ResponseEntity.ok(Map.of(
                            "mensaje", "¡Tu cuenta ha sido confirmada con éxito!",
                            "success", true
                    ));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(
                        Map.of("mensaje", "El enlace es inválido o ya fue usado.", "success", false)
                ));
    }
}
