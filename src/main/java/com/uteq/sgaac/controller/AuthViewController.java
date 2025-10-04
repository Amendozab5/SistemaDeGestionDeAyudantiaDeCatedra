package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uteq.sgaac.repository.UsuarioRepository;



@Controller
public class AuthViewController {

    private final UsuarioRepository usuarioRepository;

    public AuthViewController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 Vista de Login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // busca login.html en templates
    }

    // 🔹 Vista Home después del login
    @GetMapping("/homeEstudiante")
    public String mostrarHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        usuario_sistema usuario = usuarioRepository.findByUsername(currentUsername)
                .orElse(null);

        if (usuario != null) {
            model.addAttribute("username", usuario.getUsername());
            model.addAttribute("userInitial", usuario.getNombres().substring(0, 1).toUpperCase());
            model.addAttribute("fotoUrl", usuario.getFotoUrl());
            model.addAttribute("userRole", usuario.getRol().getNombre()); // Añadir rol al modelo
        } else {
            model.addAttribute("username", "Estudiante");
            model.addAttribute("userInitial", "E");
            model.addAttribute("fotoUrl", null);
            model.addAttribute("userRole", "ESTUDIANTE"); // Rol por defecto
        }

        return "homeEstudiante";
    }

    @GetMapping("/homeAdmin")
    public String homeAdmin(@RequestParam(name = "username", required = false) String username, Model model) {
        String displayUsername = (username != null && !username.isEmpty()) ? username : "Admin";
        model.addAttribute("username", displayUsername);
        model.addAttribute("userInitial", displayUsername.substring(0, 1).toUpperCase());
        return "homeAdmin";
    }

    @GetMapping("/homeDocente")
    public String homeDocente(@RequestParam(name = "username", required = false) String username, Model model) {
        String displayUsername = (username != null && !username.isEmpty()) ? username : "Docente";
        model.addAttribute("username", displayUsername);
        model.addAttribute("userInitial", displayUsername.substring(0, 1).toUpperCase());
        return "homeDocente";
    }

    @GetMapping("/homeDecano")
    public String homeDecano(@RequestParam(name = "username", required = false) String username, Model model) {
        String displayUsername = (username != null && !username.isEmpty()) ? username : "Decano";
        model.addAttribute("username", displayUsername);
        model.addAttribute("userInitial", displayUsername.substring(0, 1).toUpperCase());
        return "homeDecano";
    }

    @GetMapping("/homeCoordinador")
    public String homeCoordinador(@RequestParam(name = "username", required = false) String username, Model model) {
        String displayUsername = (username != null && !username.isEmpty()) ? username : "Coordinador";
        model.addAttribute("username", displayUsername);
        model.addAttribute("userInitial", displayUsername.substring(0, 1).toUpperCase());
        return "homeCoordinador";
    }

    // 🔹 Verificación de cuenta con token
    @GetMapping("/verify")
    public String verificarCuenta(@RequestParam("token") String token, Model model) {
        return usuarioRepository.findByTokenConfirmacion(token)
                .map(usuario -> {
                    usuario.setConfirmado(true);
                    usuario.setTokenConfirmacion(null);
                    usuarioRepository.save(usuario);

                    model.addAttribute("mensaje", "¡Tu cuenta ha sido confirmada con éxito!");
                    model.addAttribute("success", true);
                    return "verify"; // renderiza verify.html
                })
                .orElseGet(() -> {
                    model.addAttribute("mensaje", "El enlace es inválido o ya fue usado.");
                    model.addAttribute("success", false);
                    return "verify"; // renderiza verify.html
                });
    }
}
