package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.Decano;
import com.uteq.sgaac.model.Facultad;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.DecanoRepository;
import com.uteq.sgaac.repository.UsuarioRepository;
import com.uteq.sgaac.services.ConvocatoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/decano")
public class DecanoController {

    private static final Logger logger = LoggerFactory.getLogger(DecanoController.class);

    @Autowired
    private ConvocatoriaService convocatoriaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DecanoRepository decanoRepository;

    @GetMapping("/aprobaciones")
    public String gestionarAprobaciones(@RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (isFragmentRequest) {
            return "siderbarDecano/aprobaciones :: content";
        }
        
        return "redirect:/homeDecano?view=aprobaciones";
    }

    @GetMapping("/aprobaciones/plazas-pendientes")
    public String verPlazasPendientes(Model model,
                                     @RequestParam(name = "fragment", required = false) String fragment,
                                     @RequestHeader(name = "X-Partial", required = false) String isPartialHeader,
                                     RedirectAttributes redirectAttributes) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logger.info("Verificando perfil de Decano para el usuario: {}", username);

        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
            return "redirect:/homeDecano";
        }

        usuario_sistema usuario = usuarioOpt.get();
        logger.info("Usuario encontrado con ID: {}. Buscando en la tabla 'decano'...", usuario.getIdUsuario());

        Optional<Decano> decanoOpt = decanoRepository.findByUsuario(usuario);
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (decanoOpt.isEmpty()) {
            logger.error("Perfil de Decano no encontrado para el usuario: {}", username);
            if (isFragmentRequest) {
                return "siderbarDecano/error-fragment :: content";
            }
            redirectAttributes.addFlashAttribute("errorMessage", "No se encontró un perfil de Decano para su usuario. Por favor, contacte al administrador.");
            return "redirect:/homeDecano";
        }

        Decano decano = decanoOpt.get();
        Facultad facultad = decano.getFacultad();

        model.addAttribute("plazas", convocatoriaService.findPlazasPendientesPorFacultad(facultad));

        if (isFragmentRequest) {
            return "siderbarDecano/plazas-pendientes :: content";
        }
        
        return "redirect:/homeDecano?view=plazas-pendientes";
    }

    @GetMapping("/aprobaciones/historial")
    public String verHistorialAprobaciones(Model model,
                                     @RequestParam(name = "fragment", required = false) String fragment,
                                     @RequestHeader(name = "X-Partial", required = false) String isPartialHeader,
                                     RedirectAttributes redirectAttributes) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado.");
            return "redirect:/homeDecano";
        }
        
        usuario_sistema usuario = usuarioOpt.get();
        
        Optional<Decano> decanoOpt = decanoRepository.findByUsuario(usuario);
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (decanoOpt.isEmpty()) {
            logger.error("Perfil de Decano no encontrado para el usuario: {}", username);
            if (isFragmentRequest) {
                return "siderbarDecano/error-fragment :: content";
            }
            redirectAttributes.addFlashAttribute("errorMessage", "No se encontró un perfil de Decano para su usuario. Por favor, contacte al administrador.");
            return "redirect:/homeDecano";
        }
        
        Decano decano = decanoOpt.get();
        Facultad facultad = decano.getFacultad();

        model.addAttribute("plazasAprobadas", convocatoriaService.findPlazasAprobadasPorFacultad(facultad));

        if (isFragmentRequest) {
            return "siderbarDecano/historial-decisiones :: content";
        }
        
        return "redirect:/homeDecano?view=historial-decisiones";
    }

    @PostMapping("/aprobaciones/aprobar/{id}")
    public String aprobarPlaza(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            convocatoriaService.aprobarPlaza(id);
            redirectAttributes.addFlashAttribute("successMessage", "Plaza aprobada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al aprobar la plaza.");
        }
        return "redirect:/homeDecano?view=plazas-pendientes";
    }

    @PostMapping("/aprobaciones/rechazar/{id}")
    public String rechazarPlaza(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            convocatoriaService.rechazarPlaza(id);
            redirectAttributes.addFlashAttribute("successMessage", "Plaza rechazada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al rechazar la plaza.");
        }
        return "redirect:/homeDecano?view=plazas-pendientes";
    }
}
