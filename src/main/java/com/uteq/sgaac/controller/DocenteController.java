package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.PostulacionInfoDTO;
import com.uteq.sgaac.dto.TribunalInfoDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import com.uteq.sgaac.services.PruebaOposicionEvalService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private DatosEstudiantilRepository datosEstudiantilRepository;

    @Autowired
    private PruebaOposicionTribunalRepository pruebaOposicionTribunalRepository;

    @Autowired
    private PruebaOposicionRepository pruebaOposicionRepository;

    @Autowired
    private PruebaOposicionEvalService pruebaOposicionEvalService;


    @GetMapping("/aprobaciones-postulaciones")
    public String mostrarAprobacionesPostulaciones(Model model, @RequestParam(required = false) Boolean fragment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }
        usuario_sistema usuario = usuarioOpt.get();

        Optional<Docente> docenteOpt = docenteRepository.findByUsuario(usuario);
        if (docenteOpt.isEmpty()) {
            return "redirect:/error";
        }
        Docente docente = docenteOpt.get();

        List<Asignatura> asignaturasDelDocente = asignaturaRepository.findByDocente(docente);
        
        List<Long> asignaturaIds = asignaturasDelDocente.stream()
                                        .map(Asignatura::getIdAsignatura)
                                        .collect(Collectors.toList());

        List<Postulacion> postulacionesPendientes = postulacionRepository.findPostulacionesPendientesByAsignaturas(
            PostulacionEstado.EN_REVISION, asignaturaIds
        );

        List<PostulacionInfoDTO> postulacionesInfo = new ArrayList<>();
        for (Postulacion p : postulacionesPendientes) {
            Optional<DatosEstudiantil> datosOpt = datosEstudiantilRepository.findByEstudianteAndAsignatura(p.getEstudiante(), p.getAsignatura());
            BigDecimal calificacion = datosOpt.map(DatosEstudiantil::getCalificaciones).orElse(null);

            String estudianteNombre = p.getEstudiante().getUsuario().getNombres() + " " + p.getEstudiante().getUsuario().getApellidos();
            String asignaturaNombre = p.getAsignatura().getNombre();

            postulacionesInfo.add(new PostulacionInfoDTO(p, estudianteNombre, asignaturaNombre, calificacion));
        }

        model.addAttribute("postulacionesInfo", postulacionesInfo);

        if (fragment != null && fragment) {
            return "docente/aprobaciones-postulaciones :: content";
        }
        return "docente/aprobaciones-postulaciones";
    }

    @PostMapping("/postulaciones/aprobar/{id}")
    public String aprobarPostulacion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);
        if (postulacionOpt.isPresent()) {
            Postulacion postulacion = postulacionOpt.get();
            postulacion.setEstadoPostulacion(PostulacionEstado.APROBADO);
            postulacionRepository.save(postulacion);
            redirectAttributes.addFlashAttribute("successMessage", "Postulación aprobada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la postulación.");
        }
        return "redirect:/homeDocente?view=aprobaciones-postulaciones";
    }

    @PostMapping("/postulaciones/rechazar/{id}")
    public String rechazarPostulacion(@PathVariable Long id, @RequestParam("motivo") String motivo, RedirectAttributes redirectAttributes) {
        Optional<Postulacion> postulacionOpt = postulacionRepository.findById(id);
        if (postulacionOpt.isPresent()) {
            Postulacion postulacion = postulacionOpt.get();
            postulacion.setEstadoPostulacion(PostulacionEstado.RECHAZADO);
            postulacion.setMotivoRechazo(motivo);
            postulacionRepository.save(postulacion);
            redirectAttributes.addFlashAttribute("successMessage", "Postulación rechazada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la postulación.");
        }
        return "redirect:/homeDocente?view=aprobaciones-postulaciones";
    }

    @GetMapping("/tribunal")
    public String tribunal(Model model, @RequestParam(name = "fragment", required = false) boolean fragment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login";
        }
        usuario_sistema usuario = usuarioOpt.get();

        Optional<Docente> docenteOpt = docenteRepository.findByUsuario(usuario);
        if (docenteOpt.isEmpty()) {
            return "redirect:/error";
        }
        Docente docente = docenteOpt.get();

        List<PruebaOposicionTribunal> tribunales = pruebaOposicionTribunalRepository.findByDocente(docente);

        List<TribunalInfoDTO> tribunalInfoList = new ArrayList<>();
        for (PruebaOposicionTribunal tribunal : tribunales) {
            try {
                PruebaOposicion prueba = tribunal.getPruebaOposicion();
                Postulacion postulacion = prueba.getPostulacion();
                postulacion.getIdPostulacion(); // Trigger lazy loading

                String postulanteNombre = postulacion.getEstudiante().getUsuario().getNombres() + " " + postulacion.getEstudiante().getUsuario().getApellidos();
                String asignaturaNombre = postulacion.getAsignatura().getNombre();
                tribunalInfoList.add(new TribunalInfoDTO(prueba, postulanteNombre, asignaturaNombre));
            } catch (EntityNotFoundException e) {
                System.err.println("Data integrity issue: Could not find Postulacion for PruebaOposicion with id: " + tribunal.getPruebaOposicion().getIdOposicion());
            }
        }

        model.addAttribute("tribunales", tribunalInfoList);

        if (fragment) {
            return "docente/tribunal :: content";
        }
        return "docente/tribunal";
    }

    @PostMapping("/tribunal/calificar/{idOposicion}")
    public String calificarOposicion(@PathVariable Long idOposicion,
                                     @RequestParam("material_puntos") BigDecimal materialPuntos,
                                     @RequestParam("calidad_puntos") BigDecimal calidadPuntos,
                                     @RequestParam("respuestas_puntos") BigDecimal respuestasPuntos,
                                     @RequestParam("comentarios") String comentarios,
                                     RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);
        Optional<Docente> docenteOpt = usuarioOpt.flatMap(docenteRepository::findByUsuario);

        if (docenteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se pudo identificar al docente.");
            return "redirect:/homeDocente?view=tribunal";
        }

        Optional<PruebaOposicion> oposicionOpt = pruebaOposicionRepository.findById(idOposicion);
        if (oposicionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: No se encontró la prueba de oposición.");
            return "redirect:/homeDocente?view=tribunal";
        }

        PruebaOposicionEval evaluacion = new PruebaOposicionEval();
        evaluacion.setOposicion(oposicionOpt.get());
        evaluacion.setDocente(docenteOpt.get());
        evaluacion.setMaterialPuntos(materialPuntos);
        evaluacion.setCalidadPuntos(calidadPuntos);
        evaluacion.setRespuestasPuntos(respuestasPuntos);
        evaluacion.setComentarios(comentarios);

        BigDecimal puntajeTotal = materialPuntos.add(calidadPuntos).add(respuestasPuntos);
        evaluacion.setPuntajeTotal(puntajeTotal);

        pruebaOposicionEvalService.guardarCalificacionIndividual(evaluacion);

        redirectAttributes.addFlashAttribute("successMessage", "Calificación guardada exitosamente.");
        return "redirect:/homeDocente?view=tribunal";
    }
}
