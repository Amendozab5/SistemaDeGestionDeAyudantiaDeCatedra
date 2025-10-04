package com.uteq.sgaac.config;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyectamos para buscar al usuario

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        // 1. Obtener el usuario desde la base de datos
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        usuario_sistema usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado en la BD"));

        // Log para depuración
        logger.info("Usuario '{}' ha iniciado sesión. Chequeando condiciones: !isConfirmado() -> {}", 
            usuario.getUsername(), !usuario.isConfirmado());

        // 2. Verificar si debe cambiar la contraseña (si no está confirmado)
        if (!usuario.isConfirmado()) {
            response.sendRedirect("/change-password"); // Redirigir a la página de cambio
            return;
        }

        // 3. Si ya la cambió, redirigir por rol
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.equals("ADMIN")) {
                response.sendRedirect("/homeAdmin");
                return;
            } else if (role.equals("DOCENTE")) {
                response.sendRedirect("/homeDocente");
                return;
            } else if (role.equals("ESTUDIANTE")) {
                response.sendRedirect("/homeEstudiante");
                return;
            } else if (role.equals("DECANO")) {
                response.sendRedirect("/homeDecano");
                return;
            } else if (role.equals("COORDINADOR")) {
                response.sendRedirect("/homeCoordinador");
                return;
            }
        }
        
        // Fallback por si el usuario no tiene un rol conocido
        response.sendRedirect("/login?error");
    }
}