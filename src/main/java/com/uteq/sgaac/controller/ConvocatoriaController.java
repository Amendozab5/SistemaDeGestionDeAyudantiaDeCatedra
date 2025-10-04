package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.ConvocatoriaDTO;
import com.uteq.sgaac.model.Convocatoria;
import com.uteq.sgaac.services.ConvocatoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/convocatorias")
public class ConvocatoriaController {

    private static final Logger logger = LoggerFactory.getLogger(ConvocatoriaController.class);

    private final ConvocatoriaService convocatoriaService;

    public ConvocatoriaController(ConvocatoriaService convocatoriaService) {
        this.convocatoriaService = convocatoriaService;
    }

    @GetMapping("/all")
    @ResponseBody
    public List<ConvocatoriaDTO> getAllConvocatorias() {
        return convocatoriaService.getAllConvocatorias();
    }

    @GetMapping
    public String showConvocatoriasMenu(@RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragment = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragment) {
            return "siderbar/CrearConvocatorias :: content";
        }
        return "siderbar/CrearConvocatorias";
    }

    @GetMapping("/crear-form")
    public String showCrearForm(@RequestParam(name = "fragment", required = false) String fragment,
                                @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragment = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragment) {
            return "siderbar/convocatorias/crear-form :: content";
        }
        return "siderbar/convocatorias/crear-form";
    }

    @GetMapping("/gestionar-list")
    public String showGestionarList(Model model) {
        try {
            List<ConvocatoriaDTO> convocatorias = convocatoriaService.getAllConvocatorias();
            model.addAttribute("convocatorias", convocatorias);
        } catch (Exception e) {
            logger.error("Error al cargar las convocatorias", e);
            model.addAttribute("error", "Error al cargar las convocatorias.");
        }
        return "siderbar/convocatorias/gestionar-list :: content";
    }

    @GetMapping("/resultados-list")
    public String showResultadosList() {
        return "siderbar/convocatorias/resultados-list :: content";
    }

    @PostMapping("/guardar")
    public String guardarConvocatoria(@ModelAttribute Convocatoria convocatoria,
                                      @RequestParam("action") String action,
                                      RedirectAttributes redirectAttributes,
                                      Principal principal) {
        try {
            boolean isPublish = "publish".equals(action);
            convocatoria.setEstado(isPublish);
            convocatoriaService.save(convocatoria);

            if (isPublish) {
                redirectAttributes.addFlashAttribute("successMessage", "Convocatoria publicada con éxito.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Convocatoria guardada como borrador.");
            }
        } catch (Exception e) {
            logger.error("Error al guardar la convocatoria", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la convocatoria.");
        }
        String username = principal.getName();
        return "redirect:/homeAdmin?view=convocatorias-menu";
    }

    @GetMapping("/editar/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model,
                               @RequestParam(name = "fragment", required = false) String fragment,
                               @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        Convocatoria convocatoria = convocatoriaService.getConvocatoriaById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid convocatoria Id:" + id));
        model.addAttribute("convocatoria", convocatoria);

        boolean isFragment = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragment) {
            return "siderbar/convocatorias/editar-form :: content";
        }
        return "siderbar/convocatorias/editar-form";
    }

    @PostMapping("/actualizar")
    public String actualizarConvocatoria(@ModelAttribute Convocatoria convocatoria, RedirectAttributes redirectAttributes) {
        try {
            convocatoriaService.save(convocatoria);
            redirectAttributes.addFlashAttribute("successMessage", "Convocatoria actualizada con éxito.");
        } catch (Exception e) {
            logger.error("Error al actualizar la convocatoria", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la convocatoria.");
        }
        // Redirect to the main view where the list is displayed
        return "redirect:/homeAdmin?view=convocatoria-gestionar-list";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteConvocatoria(@PathVariable("id") Integer id) {
        try {
            convocatoriaService.deleteConvocatoria(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting convocatoria with id: " + id, e);
            return ResponseEntity.status(500).build();
        }
    }
}