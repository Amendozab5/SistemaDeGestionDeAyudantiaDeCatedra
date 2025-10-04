package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.LoginRequest;
import com.uteq.sgaac.dto.RegisterRequest;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthApiController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthApiController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    // 📌 Registro (JSON)
    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody @Valid RegisterRequest request) {
        try {
            usuario_sistema usuario = authService.registrar(request);
            return ResponseEntity.ok(Map.of(
                    "id", usuario.getIdUsuario(),
                    "email", usuario.getEmail(),
                    "rol", usuario.getRol().getNombre().toLowerCase(),
                    "activo", usuario.isActivo()
            ));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", ex.getMessage()
            ));
        } catch (Exception ex) {
            ex.printStackTrace(); //
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error inesperado al registrar"
            ));
        }
    }

    // 📌 Login (JSON con DTO)
    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);

            usuario_sistema usuario = authService.loginPorUsuario(
                    request.getUsername(),
                    request.getPassword()
            );

            // Registrar último acceso
            authService.recordLogin(usuario);

            return ResponseEntity.ok(Map.of(    
                    "id", usuario.getIdUsuario(),
                    "nombre", usuario.getNombres() + " " + usuario.getApellidos(), // ✅ más confiable que username
                    "email", usuario.getEmail(),
                    "rol", usuario.getRol().getNombre().toLowerCase(),
                    "activo", usuario.isActivo()
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Credenciales inválidas"
            ));
        }
    }
}
