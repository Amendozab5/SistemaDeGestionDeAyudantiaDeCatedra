package com.uteq.sgaac.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uteq.sgaac.dto.AgendarPruebaDTO;
import com.uteq.sgaac.dto.AgendarPruebaGrupoDTO;
import com.uteq.sgaac.dto.AsignarTribunalDTO;
import com.uteq.sgaac.dto.CrearPlazasForm;
import com.uteq.sgaac.dto.FinalizacionDetalleDTO;
import com.uteq.sgaac.dto.GuardarMeritosDTO;
import com.uteq.sgaac.dto.MeritosDetalleDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import com.uteq.sgaac.services.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.uteq.sgaac.dto.PostulacionDTO;
import com.uteq.sgaac.dto.PostulacionDocumentoDTO;
import com.uteq.sgaac.dto.RequisitoDTO;
import java.util.stream.Collectors;
import java.util.Set;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private static final Logger logger = LoggerFactory.getLogger(CoordinadorController.class);

    private final ConvocatoriaRepository convocatoriaRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final ConvocatoriaService convocatoriaService;
    private final PostulacionRepository postulacionRepository;
    private final PruebaOposicionRepository pruebaOposicionRepository;
    private final DocenteRepository docenteRepository;
    private final PruebaOposicionEvalService pruebaOposicionEvalService;
    private final UsuarioRepository usuarioRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final PostulacionService postulacionService;
    private final PruebaOposicionService pruebaOposicionService;
    private final EvaluacionMeritosService evaluacionMeritosService;
    private final ObjectMapper objectMapper;
    private final PruebaOposicionEvalRepository pruebaOposicionEvalRepository;


    public CoordinadorController(ConvocatoriaRepository convocatoriaRepository, AsignaturaRepository asignaturaRepository, ConvocatoriaService convocatoriaService, PostulacionRepository postulacionRepository, PruebaOposicionRepository pruebaOposicionRepository, DocenteRepository docenteRepository, PruebaOposicionEvalService pruebaOposicionEvalService, UsuarioRepository usuarioRepository, CoordinadorRepository coordinadorRepository, PostulacionService postulacionService, PruebaOposicionService pruebaOposicionService, EvaluacionMeritosService evaluacionMeritosService, ObjectMapper objectMapper, PruebaOposicionEvalRepository pruebaOposicionEvalRepository) {
        this.convocatoriaRepository = convocatoriaRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.convocatoriaService = convocatoriaService;
        this.postulacionRepository = postulacionRepository;
        this.pruebaOposicionRepository = pruebaOposicionRepository;
        this.docenteRepository = docenteRepository;
        this.pruebaOposicionEvalService = pruebaOposicionEvalService;
        this.usuarioRepository = usuarioRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.postulacionService = postulacionService;
        this.pruebaOposicionService = pruebaOposicionService;
        this.evaluacionMeritosService = evaluacionMeritosService;
        this.objectMapper = objectMapper;
        this.pruebaOposicionEvalRepository = pruebaOposicionEvalRepository;
    }

    @GetMapping
    public String homeCoordinador(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("userInitial", principal.getName().substring(0, 1).toUpperCase());
        return "homeCoordinador";
    }

    private boolean isFragmentRequest(String fragment, String isPartialHeader) {
        return "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
    }

    @GetMapping("/evaluar-meritos")
    public String mostrarEvaluarMeritos(Model model, @RequestParam(name = "fragment", required = false) String fragment,
                                      @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        
        List<EvaluacionMeritos> evaluacionesPendientes = evaluacionMeritosService.findEvaluacionesPendientes();
        Map<String, List<EvaluacionMeritos>> evaluacionesPorConvocatoria = evaluacionesPendientes.stream()
                .collect(Collectors.groupingBy(e -> e.getPostulacion().getConvocatoria().getTitulo()));
        model.addAttribute("evaluacionesPorConvocatoria", evaluacionesPorConvocatoria);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/evaluar-meritos :: content";
        }
        return "siderbarCoordinador/evaluar-meritos";
    }

    @GetMapping("/meritos/detalles/{id}")
    @ResponseBody
    public ResponseEntity<?> getMeritosDetalles(@PathVariable Long id) {
        try {
            MeritosDetalleDTO dto = evaluacionMeritosService.getDetallesParaEvaluacion(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/meritos/guardar/{id}")
    @ResponseBody
    public ResponseEntity<?> guardarMeritos(@PathVariable Long id, @RequestBody GuardarMeritosDTO dto) {
        try {
            evaluacionMeritosService.guardarEvaluacion(id, dto);
            return ResponseEntity.ok(Map.of("message", "Evaluación de méritos guardada con éxito."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error al guardar la evaluación de méritos para el ID: " + id, e);
            return ResponseEntity.badRequest().body(Map.of("error", "Ocurrió un error al guardar la evaluación."));
        }
    }

    @GetMapping("/evaluar-pruebas")
    public String evaluarPruebas(Model model, @RequestParam(name = "fragment", required = false) String fragment,
                               @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        List<String> statuses = List.of("AGENDADA", "PENDIENTE_CIERRE");
        List<PruebaOposicion> pruebasPendientes = pruebaOposicionRepository.findByEstadoInWithDetails(statuses);
        model.addAttribute("pruebas", pruebasPendientes);

        Map<Long, Boolean> evaluacionCompletaMap = pruebasPendientes.stream()
                .collect(Collectors.toMap(
                        PruebaOposicion::getIdOposicion,
                        prueba -> {
                            int tribunalSize = prueba.getTribunal().size();
                            int evalSize = pruebaOposicionEvalRepository.findByOposicion(prueba).size();
                            return tribunalSize == evalSize && tribunalSize > 0;
                        }
                ));
        model.addAttribute("evaluacionCompletaMap", evaluacionCompletaMap);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/evaluar-pruebas :: content";
        }
        return "siderbarCoordinador/evaluar-pruebas";
    }

    @GetMapping("/finalizacion-detalles/{postulacionId}")
    @ResponseBody
    public ResponseEntity<?> getFinalizacionDetalles(@PathVariable Long postulacionId) {
        try {
            FinalizacionDetalleDTO dto = postulacionService.getDetallesFinalizacion(postulacionId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error al obtener detalles de finalización para postulación ID: " + postulacionId, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/finalizar-proceso/{postulacionId}")
    @ResponseBody
    public ResponseEntity<?> finalizarProceso(@PathVariable Long postulacionId) {
        try {
            postulacionService.finalizarPostulacion(postulacionId);
            return ResponseEntity.ok(Map.of("message", "Proceso finalizado con éxito."));
        } catch (Exception e) {
            logger.error("Error al finalizar el proceso para postulación ID: " + postulacionId, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/oposicion/{id}/evaluar-modal")
    public String getEvaluarOposicionModal(@PathVariable Long id, Model model) {
        PruebaOposicion prueba = pruebaOposicionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prueba de oposición no encontrada con ID: " + id));
        
        Carrera carrera = prueba.getPostulacion().getAsignatura().getCarrera();
        List<Docente> docentesPosibles = docenteRepository.findByCarrera(carrera);

        model.addAttribute("prueba", prueba);
        model.addAttribute("docentesPosibles", docentesPosibles);

        return "siderbarCoordinador/evaluar-oposicion-modal :: content";
    }

    @PostMapping("/oposicion/{id}/evaluar")
    public String procesarEvaluacionOposicion(@PathVariable Long id, PruebaOposicionEvalWrapper wrapper, RedirectAttributes redirectAttributes) {
        logger.info("Procesando evaluación para la prueba ID: {}. Se recibieron {} evaluaciones del formulario.", id, wrapper.getEvaluaciones() != null ? wrapper.getEvaluaciones().size() : 0);
        try {
            pruebaOposicionEvalService.guardarYCalcularResultados(id, wrapper.getEvaluaciones());
            redirectAttributes.addFlashAttribute("successMessage", "Evaluación guardada y finalizada con éxito.");
        } catch (Exception e) {
            logger.error("Error al procesar la evaluación de la oposición con ID: " + id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la evaluación.");
        }
        return "redirect:/coordinador?view=evaluar-pruebas";
    }

    @GetMapping("/convocatorias")
    public String gestionarConvocatorias(@RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/convocatorias :: content";
        }
        return "siderbarCoordinador/convocatorias";
    }

    @GetMapping("/publicar")
    public String publicarConvocatorias(Model model, @RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        model.addAttribute("convocatorias", convocatoriaService.findConvocatoriasListasParaPublicar());
        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/publicar-convocatorias :: content";
        }
        return "siderbarCoordinador/publicar-convocatorias";
    }

    @PostMapping("/publicar/{id}")
    public String publicarConvocatoria(@PathVariable Long id,
                                       @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                       @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                       RedirectAttributes redirectAttributes) {
        try {
            convocatoriaService.publicarConvocatoria(id, fechaInicio, fechaFin);
            redirectAttributes.addFlashAttribute("successMessage", "Convocatoria publicada con éxito.");
        } catch (Exception e) {
            logger.error("Error al publicar la convocatoria", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al publicar la convocatoria.");
        }
        return "redirect:/coordinador/publicar";
    }


    @GetMapping("/postulaciones")
    public String gestionarPostulaciones(@RequestParam(name = "fragment", required = false) String fragment,
                                         @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/postulaciones :: content";
        }
        return "siderbarCoordinador/postulaciones";
    }

    @GetMapping("/postulaciones/crear")
    public String crearPostulacionForm(Model model, @RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        List<Convocatoria> convocatoriasActivas = convocatoriaRepository.findByEstado(true);
        model.addAttribute("convocatorias", convocatoriasActivas);

        List<Integer> semestres = asignaturaRepository.findDistinctSemestres();
        model.addAttribute("semestres", semestres);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/crearPostulacion :: content";
        }
        return "siderbarCoordinador/crearPostulacion";
    }

    @PostMapping("/postulaciones/crear")
    public String crearPostulacion(@ModelAttribute CrearPlazasForm form, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Buscando usuario: {}", principal.getName());
            usuario_sistema usuario = usuarioRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            logger.info("Usuario encontrado: {}", usuario.getUsername());

            logger.info("Buscando coordinador para el usuario: {}", usuario.getUsername());
            Coordinador coordinador = coordinadorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Coordinador no encontrado para el usuario"));
            logger.info("Coordinador encontrado: {}", coordinador.getId());

            convocatoriaService.crearPlazas(form, coordinador);
            redirectAttributes.addFlashAttribute("successMessage", "Plazas creadas con éxito.");
        } catch (Exception e) {
            logger.error("Error al crear las plazas", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear las plazas: " + e.getMessage());
        }
        return "redirect:/coordinador?view=postulaciones&username=" + principal.getName();
    }

    @GetMapping("/postulaciones/revisar")
    public String revisarPostulaciones(Model model, @RequestParam(name = "fragment", required = false) String fragment,
                                     @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        
        List<Postulacion> postulaciones = postulacionService.findAll();
        Map<String, List<PostulacionDTO>> postulacionesPorConvocatoria = postulaciones.stream().map(p -> {
            Set<PostulacionDocumentoDTO> documentosDTO = p.getDocumentos().stream().map(d -> {
                RequisitoDTO requisitoDTO = new RequisitoDTO(d.getRequisito().getId(), d.getRequisito().getDescripcion(), d.getRequisito().isActivo(), d.getRequisito().getTipo(), d.getRequisito().getUrlPlantilla());
                return new PostulacionDocumentoDTO(d.getId(), d.getTipoDocumento(), d.isEsValido(), d.getRutaArchivo(), requisitoDTO, d.getPuntajeAsignado(), d.getObservaciones());
            }).collect(Collectors.toSet());

            return new PostulacionDTO(
                    p.getIdPostulacion(),
                    p.getEstudiante().getUsuario().getNombres() + " " + p.getEstudiante().getUsuario().getApellidos(),
                    p.getEstudiante().getUsuario().getEmail(),
                    p.getAsignatura().getNombre(),
                    p.getEstadoPostulacion().name(),
                    documentosDTO,
                    p.getConvocatoria().getTitulo()
            );
        }).collect(Collectors.groupingBy(PostulacionDTO::getConvocatoriaTitulo));

        model.addAttribute("postulacionesPorConvocatoria", postulacionesPorConvocatoria);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/revisar-postulaciones :: content";
        }
        return "siderbarCoordinador/revisar-postulaciones";
    }

    @PostMapping("/postulaciones/asignar-tribunal")
    @ResponseBody
    public ResponseEntity<?> asignarTribunal(@RequestBody AsignarTribunalDTO dto) {
        try {
            postulacionService.asignarTribunal(dto);
            return ResponseEntity.ok(Map.of("message", "Tribunal asignado con éxito."));
        } catch (Exception e) {
            logger.error("Error al asignar tribunal", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ayudantias")
    public String gestionarAyudantias(Model model, HttpServletRequest request, @RequestParam(name = "fragment", required = false) String fragment,
                                      @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("csrf_token", csrfToken.getToken());
            model.addAttribute("csrf_header", csrfToken.getHeaderName());
        }

        List<Postulacion> ganadores = postulacionRepository.findGanadoresNoConvertidos();
        logger.info("DEBUG: Encontrados {} ganadores no convertidos.", ganadores.size());
        model.addAttribute("ganadores", ganadores);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/ayudantias :: content";
        }
        return "siderbarCoordinador/ayudantias";
    }

    @PostMapping("/ayudantes/convertir/{postulacionId}")
    @ResponseBody
    public ResponseEntity<?> convertirEnAyudante(@PathVariable Long postulacionId) {
        try {
            postulacionService.convertirGanadorEnAyudante(postulacionId);
            return ResponseEntity.ok(Map.of("message", "Estudiante convertido a Ayudante de Cátedra con éxito."));
        } catch (Exception e) {
            logger.error("Error al convertir a ayudante para postulación ID: " + postulacionId, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/reportes")
    public String generarReportes(@RequestParam(name = "fragment", required = false) String fragment,
                                  @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/reportes :: content";
        }
        return "siderbarCoordinador/reportes";
    }

    @GetMapping("/configuracion")
    public String mostrarConfiguracion(@RequestParam(name = "fragment", required = false) String fragment,
                                       @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/configuracion :: content";
        }
        return "siderbarCoordinador/configuracion";
    }

    @GetMapping("/pruebas-oposicion")
    public String gestionarPruebasOposicion(Model model,
                                          HttpServletRequest request, // Añadir HttpServletRequest
                                          @RequestParam(name = "fragment", required = false) String fragment,
                                          @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        // Añadir token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken != null) {
            model.addAttribute("csrf_token", csrfToken.getToken());
            model.addAttribute("csrf_header", csrfToken.getHeaderName());
        }

        List<Postulacion> postulacionesAprobadas = postulacionRepository.findByEstadoPostulacionWithDetails(PostulacionEstado.APROBADO);
        Map<Asignatura, List<Postulacion>> postulacionesAgrupadas = postulacionesAprobadas.stream()
                .collect(Collectors.groupingBy(Postulacion::getAsignatura));
        model.addAttribute("postulacionesAgrupadas", postulacionesAgrupadas);

        Map<Long, String> postulacionesJsonMap = new HashMap<>();
        for (Map.Entry<Asignatura, List<Postulacion>> entry : postulacionesAgrupadas.entrySet()) {
            List<Map<String, Object>> simpleList = entry.getValue().stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", p.getIdPostulacion());
                        map.put("name", p.getEstudiante().getUsuario().getNombres() + " " + p.getEstudiante().getUsuario().getApellidos());
                        return map;
                    })
                    .collect(Collectors.toList());
            try {
                postulacionesJsonMap.put(entry.getKey().getIdAsignatura(), objectMapper.writeValueAsString(simpleList));
            } catch (JsonProcessingException e) {
                logger.error("Error serializing postulaciones for asignatura ID: " + entry.getKey().getIdAsignatura(), e);
            }
        }
        model.addAttribute("postulacionesJsonMap", postulacionesJsonMap);


        List<Docente> allDocentes = docenteRepository.findAll();
        model.addAttribute("docentes", allDocentes);

        if (isFragmentRequest(fragment, isPartialHeader)) {
            return "siderbarCoordinador/pruebas-oposicion :: content";
        }
        return "siderbarCoordinador/pruebas-oposicion";
    }

    @PostMapping("/agendar-prueba")
    public String agendarPrueba(@ModelAttribute AgendarPruebaDTO dto, RedirectAttributes redirectAttributes) {
        try {
            pruebaOposicionService.agendarPrueba(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Prueba de oposición agendada y notificada con éxito.");
        } catch (Exception e) {
            logger.error("Error al agendar prueba de oposición", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error al agendar prueba de oposición: " + e.getMessage());
        }
        return "redirect:/coordinador?view=pruebas-oposicion";
    }

    @PostMapping("/postulaciones/{id}/aprobar")
    @ResponseBody
    public ResponseEntity<?> aprobarPostulacion(@PathVariable Long id) {
        try {
            postulacionService.aprobarPostulacion(id);
            return ResponseEntity.ok(Map.of("message", "Postulación aprobada con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/postulaciones/{id}/rechazar")
    @ResponseBody
    public ResponseEntity<?> rechazarPostulacion(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String motivo = payload.get("motivo");
            postulacionService.rechazarPostulacion(id, motivo);
            return ResponseEntity.ok(Map.of("message", "Postulación rechazada con éxito."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/postulaciones/documento/validar")
    @ResponseBody
    public ResponseEntity<?> validarDocumentoPostulacion(@RequestParam Long postulacionId, @RequestParam String tipoDocumento, @RequestParam boolean esValido) {
        try {
            postulacionService.actualizarEstadoDocumento(postulacionId, tipoDocumento, esValido);
            return ResponseEntity.ok(Map.of("message", "Estado del documento actualizado con éxito."));
        } catch (Exception e) {
            logger.error("Error al actualizar estado del documento para postulación ID: " + postulacionId, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

