package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.AsistenciaForm;
import com.uteq.sgaac.dto.EstudianteDashboardDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import com.uteq.sgaac.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    private PlazaService plazaService;

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private AyudanteCatedraRepository ayudanteCatedraRepository;

    @Autowired
    private ActividadRefuerzoService actividadRefuerzoService;

    @Autowired
    private ActividadRefuerzoRepository actividadRefuerzoRepository;

    @Autowired
    private RequisitoRepository requisitoRepository; 

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PruebaOposicionService pruebaOposicionService;

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private DatosEstudiantilRepository datosEstudiantilRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @GetMapping("/dashboard-data")
    @ResponseBody
    public ResponseEntity<?> getDashboardData(Principal principal) {
        String username = principal.getName();
        logger.info("Buscando datos del dashboard para el usuario: {}", username);

        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        
        logger.info("Usuario encontrado: {}", usuario);

        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            logger.warn("No se encontró un estudiante asociado al usuario: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado");
        }
        
        Estudiante estudiante = estudianteOpt.get();
        logger.info("Estudiante encontrado: {}", estudiante);

        long activeApplications = postulacionRepository.countByEstudianteAndEstadoPostulacionIn(
            estudiante, 
            Arrays.asList(PostulacionEstado.EN_REVISION, PostulacionEstado.APROBADO)
        );

        long scheduledTests = pruebaOposicionService.findByEstudiante(estudiante).size();

        EstudianteDashboardDTO dashboardData = new EstudianteDashboardDTO(
            usuario.getNombres(),
            activeApplications,
            scheduledTests,
            estudiante.getPromedioGeneral()
        );

        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/mis-pruebas")
    public String getMisPruebas(Model model, Principal principal,
                                      @RequestParam(name = "fragment", required = false) String fragment,
                                      @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            model.addAttribute("error", "Estudiante no encontrado");
            model.addAttribute("pruebas", Collections.emptyList());
            return "siderbarEstudiante/mis-pruebas :: content";
        }
        Estudiante estudiante = estudianteOpt.get();

        List<PruebaOposicion> pruebas = pruebaOposicionService.findByEstudiante(estudiante);
        model.addAttribute("pruebas", pruebas);

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/mis-pruebas :: content";
        } else {
            return "redirect:/homeEstudiante?view=mis-pruebas";
        }
    }

    @Autowired
    private CriterioMeritoService criterioMeritoService;

    @GetMapping("/postulaciones")
    public String getPlazasDisponibles(Model model,
                                       @RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        model.addAttribute("plazasPorConvocatoria", plazaService.findPlazasParaEstudiante(usuario));
        model.addAttribute("requisitos", requisitoRepository.findByActivoTrue()); 
        model.addAttribute("criteriosMerito", criterioMeritoService.findByActivoTrue());

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/postular-ayudantia :: content";
        }

        return "redirect:/homeEstudiante?view=postulaciones";
    }

    @GetMapping("/perfil")
    public String getPerfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<usuario_sistema> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalStateException("Usuario no encontrado");
        }

        usuario_sistema usuario = usuarioOpt.get();
        Estudiante estudiante = estudianteRepository.findByUsuario(usuario).orElse(null);

        model.addAttribute("usuario", usuario);
        model.addAttribute("estudiante", estudiante);

        return "siderbarEstudiante/perfil";
    }

    @PostMapping("/perfil/upload-photo")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadPhoto(@RequestParam("photo") MultipartFile file,
                                                         Authentication authentication) {
        try {
            String username = authentication.getName();
            String newPhotoUrl = authService.updateUserPhoto(username, file);
            return ResponseEntity.ok(Map.of("fotoUrl", newPhotoUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                               @RequestParam("newPassword") String newPassword,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {

        String username = authentication.getName();
        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, usuario.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta.");
            return "redirect:/homeEstudiante?view=perfil";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "La nueva contraseña y la confirmación no coinciden.");
            return "redirect:/homeEstudiante?view=perfil";
        }

        authService.changePassword(username, newPassword);

        redirectAttributes.addFlashAttribute("success", "¡Contraseña actualizada exitosamente!");
        return "redirect:/homeEstudiante?view=perfil";
    }

    @Autowired
    private PostulacionService postulacionService;

    @PostMapping("/postulacion/crear")
    public String crearPostulacion(HttpServletRequest request,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {

        try {
            String username = authentication.getName();
            Long plazaId = Long.parseLong(request.getParameter("plazaId"));
            
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            java.util.Map<String, MultipartFile> files = multipartRequest.getFileMap();

            postulacionService.crearPostulacion(plazaId, username, files);
            redirectAttributes.addFlashAttribute("message",
                    "¡Postulación enviada exitosamente! Tus documentos han sido subidos.");

        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el stack trace completo en la consola
            redirectAttributes.addFlashAttribute("error", "Error al procesar la postulación: " + e.getMessage());
        }

        return "redirect:/homeEstudiante?view=postulaciones";
    }

    @GetMapping("/mis-postulaciones")
    public String getMisPostulaciones(Model model, Principal principal,
                                      @RequestParam(name = "fragment", required = false) String fragment,
                                      @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            model.addAttribute("error", "Estudiante no encontrado");
            model.addAttribute("postulaciones", Collections.emptyList());
            return "siderbarEstudiante/mis-postulaciones :: content";
        }
        Estudiante estudiante = estudianteOpt.get();

        List<Postulacion> postulaciones = postulacionRepository.findByEstudiante(estudiante);
        model.addAttribute("postulaciones", postulaciones);

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/mis-postulaciones :: content";
        } else {
            return "redirect:/homeEstudiante?view=mis-postulaciones";
        }
    }

    @GetMapping("/gestion-actividades")
    public String getGestionActividades(@RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/gestion-actividades :: content";
        }
        return "redirect:/homeEstudiante?view=gestion-actividades";
    }

    @PostMapping("/actividad/registrar")
    public String registrarActividad(@RequestParam("titulo") String titulo,
                                     @RequestParam("descripcion") String descripcion,
                                     @RequestParam("tipoActividad") String tipoActividad,
                                     @RequestParam(name = "archivo", required = false) MultipartFile archivo,
                                     @RequestParam(name = "enlace", required = false) String enlace,
                                     @RequestParam("asignaturaId") Long asignaturaId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {

        try {
            actividadRefuerzoService.createActividad(titulo, descripcion, tipoActividad, archivo, enlace, asignaturaId, principal.getName());
            redirectAttributes.addFlashAttribute("success", "¡Actividad registrada exitosamente!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/homeEstudiante?view=gestion-actividades";
    }

    @GetMapping("/ver-actividades")
    public String getVerActividades(Model model, Principal principal,
                                    @RequestParam(name = "fragment", required = false) String fragment,
                                    @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            model.addAttribute("error", "Estudiante no encontrado");
            model.addAttribute("actividades", Collections.emptyList());
            return "siderbarEstudiante/ver-actividades :: content";
        }
        Estudiante estudiante = estudianteOpt.get();

        List<ActividadRefuerzo> actividades = actividadRefuerzoRepository.findByAyudante(estudiante);
        model.addAttribute("actividades", actividades);

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/ver-actividades :: content";
        }
        return "redirect:/homeEstudiante?view=ver-actividades";
    }

    @GetMapping("/foro")
    public String getForo(@RequestParam(name = "fragment", required = false) String fragment,
                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/foro :: content";
        }
        return "redirect:/homeEstudiante?view=foro";
    }

    @GetMapping("/aula")
    public String getAula(@RequestParam(name = "fragment", required = false) String fragment,
                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/aula :: content";
        }
        return "redirect:/homeEstudiante?view=aula";
    }

    @GetMapping("/horario")
    public String getHorarioView(@RequestParam(name = "fragment", required = false) String fragment,
                                 @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/horario :: content";
        }
        return "redirect:/homeEstudiante?view=horario";
    }

    @GetMapping("/asistencia")
    public String getAsistencia(Model model, Principal principal,
                                @RequestParam(name = "fragment", required = false) String fragment,
                                @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            model.addAttribute("error", "Estudiante no encontrado");
            model.addAttribute("asignaturas", Collections.emptyList());
            model.addAttribute("estudiantes", Collections.emptyList());
            return "siderbarEstudiante/asistencia :: content";
        }
        Estudiante estudiante = estudianteOpt.get();

        List<AyudanteCatedra> ayudantias = ayudanteCatedraRepository.findByEstudiante(estudiante);
        List<Asignatura> asignaturas = ayudantias.stream()
                .map(ayudantia -> ayudantia.getPostulacion().getAsignatura())
                .collect(Collectors.toList());

        model.addAttribute("asignaturas", asignaturas);

        if (!asignaturas.isEmpty()) {
            Asignatura primeraAsignatura = asignaturas.get(0);
            List<DatosEstudiantil> datosEstudiantes = datosEstudiantilRepository.findByAsignatura(primeraAsignatura);
            List<Estudiante> estudiantes = datosEstudiantes.stream()
                    .map(DatosEstudiantil::getEstudiante)
                    .collect(Collectors.toList());
            model.addAttribute("estudiantes", estudiantes);
        } else {
            model.addAttribute("estudiantes", Collections.emptyList());
        }
        
        model.addAttribute("asistenciaForm", new AsistenciaForm());


        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/asistencia :: content";
        }
        return "redirect:/homeEstudiante?view=asistencia";
    }

    @GetMapping("/estudiantesPorAsignatura")
    @ResponseBody
    public List<Estudiante> getEstudiantesPorAsignatura(@RequestParam Long asignaturaId) {
        Asignatura asignatura = new Asignatura();
        asignatura.setIdAsignatura(asignaturaId);
        List<DatosEstudiantil> datosEstudiantes = datosEstudiantilRepository.findByAsignatura(asignatura);
        return datosEstudiantes.stream()
                .map(DatosEstudiantil::getEstudiante)
                .collect(Collectors.toList());
    }

    @PostMapping("/asistencia/guardar")
    public String guardarAsistencias(@ModelAttribute AsistenciaForm asistenciaForm, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
            Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

            if (estudianteOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Estudiante no encontrado");
                return "redirect:/estudiante/asistencia";
            }
            Estudiante estudiante = estudianteOpt.get();
            
            AyudanteCatedra ayudante = ayudanteCatedraRepository.findByEstudiante(estudiante).stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("Ayudante no encontrado"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime fechaSesion = LocalDateTime.parse(asistenciaForm.getFechaSesion(), formatter);

            asistenciaService.guardarAsistencias(asistenciaForm.getAsistencias(), ayudante.getIdAyudante(), asistenciaForm.getIdAsignatura(), fechaSesion);
            redirectAttributes.addFlashAttribute("success", "Asistencia guardada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la asistencia: " + e.getMessage());
        }
        return "redirect:/estudiante/asistencia";
    }


    @GetMapping("/mi-curso")
    public String getMiCurso(@RequestParam(name = "fragment", required = false) String fragment,
                               @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/mi-curso :: content";
        }
        return "redirect:/homeEstudiante?view=mi-curso";
    }

    @GetMapping("/reportes-aula")
    public String getReportesAula(@RequestParam(name = "fragment", required = false) String fragment,
                                 @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/reportes-aula :: content";
        }
        return "redirect:/homeEstudiante?view=reportes-aula";
    }

    @GetMapping("/registrar-actividad")
    public String getRegistrarActividad(Model model, Principal principal,
                                        @RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);

        if (estudianteOpt.isEmpty()) {
            model.addAttribute("error", "Estudiante no encontrado");
            model.addAttribute("asignaturas", Collections.emptyList());
            return "siderbarEstudiante/registrar-actividad :: content";
        }
        Estudiante estudiante = estudianteOpt.get();

        List<AyudanteCatedra> ayudantias = ayudanteCatedraRepository.findByEstudiante(estudiante);
        List<Asignatura> asignaturas = ayudantias.stream()
                                                .map(ayudantia -> ayudantia.getPostulacion().getAsignatura())
                                                .collect(Collectors.toList());
        model.addAttribute("asignaturas", asignaturas);

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/registrar-actividad :: content";
        }
        return "redirect:/homeEstudiante?view=registrar-actividad";
    }

    @GetMapping("/editar-actividad")
    public String getEditarActividad(@RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/editar-actividad :: content";
        }
        return "redirect:/homeEstudiante?view=editar-actividad";
    }

    @GetMapping("/proximas-entregas")
    public String getProximasEntregas(@RequestParam(name = "fragment", required = false) String fragment,
                                        @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbarEstudiante/proximas-entregas :: content";
        }
        return "redirect:/homeEstudiante?view=proximas-entregas";
    }
}