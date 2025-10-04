package com.uteq.sgaac.controller;

import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import com.uteq.sgaac.services.AsignaturaService;
import com.uteq.sgaac.services.AutoridadService;
import com.uteq.sgaac.services.EstudianteService;
import com.uteq.sgaac.services.UsuarioService;
import com.uteq.sgaac.dto.AsignaturaSimpleDTO;
import com.uteq.sgaac.dto.AutoridadDTO;
import com.uteq.sgaac.dto.AutoridadSimpleDTO;
import com.uteq.sgaac.dto.EstudianteDTO;
import com.uteq.sgaac.dto.UsuarioSimpleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutoridadService autoridadService;

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaService asignaturaService; // Inyectar el servicio

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @GetMapping("/estudiantes")
    public ResponseEntity<List<UsuarioSimpleDTO>> getEstudiantes() {
        return ResponseEntity.ok(usuarioService.getEstudiantes());
    }

    @GetMapping("/autoridades")
    public ResponseEntity<List<AutoridadSimpleDTO>> getAutoridades() {
        return ResponseEntity.ok(usuarioService.getAutoridades());
    }

    @PostMapping("/crear-autoridad")
    public ResponseEntity<Map<String, String>> crearAutoridad(@RequestBody AutoridadDTO autoridadDTO) {
        logger.info("Creando autoridad con DTO: {}", autoridadDTO);
        try {
            autoridadService.createAutoridad(autoridadDTO);
            return new ResponseEntity<>(Map.of("message", "Autoridad creada correctamente"), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear autoridad", e);
            return new ResponseEntity<>(Map.of("message", "Error al crear la autoridad: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutoridadSimpleDTO> getUsuario(@PathVariable Long id) {
        AutoridadSimpleDTO usuario = usuarioService.getUsuario(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSimpleDTO> updateUsuario(@PathVariable Long id, @RequestBody EstudianteDTO estudianteDTO) {
        UsuarioSimpleDTO usuario = usuarioService.updateUsuario(id, estudianteDTO);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/toggle-activo")
    public ResponseEntity<AutoridadSimpleDTO> toggleActivo(@PathVariable Long id) {
        AutoridadSimpleDTO usuario = usuarioService.toggleActivo(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-estudiante")
    public ResponseEntity<Map<String, String>> crearEstudiante(@RequestBody EstudianteDTO estudianteDTO) {
        try {
            estudianteService.createEstudiante(estudianteDTO);
            return new ResponseEntity<>(Map.of("message", "Estudiante creado correctamente"), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear estudiante", e);
            return new ResponseEntity<>(Map.of("message", "Error al crear el estudiante: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gestion")
    public String mostrarGestionUsuarios(
            @RequestParam(name = "fragment", required = false) String fragment,
            @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (isFragmentRequest) {
            logger.info("Devolviendo FRAGMENTO siderbar/GestionUsuarios :: content");
            return "siderbar/GestionUsuarios :: content";
        }
        
        logger.info("Request for full page detected. Returning 'siderbar/GestionUsuarios'");
        return "siderbar/GestionUsuarios";
    }

    @GetMapping("/ayudantias")
    public String mostrarGestionAyudantias(Model model,
                                           @RequestParam(name = "fragment", required = false) String fragment,
                                           @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {
        List<usuario_sistema> ayudantes = usuarioRepository.findByRolNombre("AYUDANTE");
        model.addAttribute("ayudantes", ayudantes);

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);
        if (isFragmentRequest) {
            return "siderbar/GestionarAyudantia :: content";
        }

        return "siderbar/GestionarAyudantia";
    }

    @GetMapping("/gestion-autoridades")
    public String mostrarGestionAutoridades(
            @RequestParam(name = "fragment", required = false) String fragment,
            @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (isFragmentRequest) {
            return "usuarios/gestion-autoridades :: content";
        }

        return "usuarios/gestion-autoridades";
    }

    @GetMapping("/crear-estudiante")
    public String mostrarCrearEstudiante(
            @RequestParam(name = "fragment", required = false) String fragment,
            @RequestHeader(name = "X-Partial", required = false) String isPartialHeader) {

        boolean isFragmentRequest = "true".equalsIgnoreCase(fragment) || "true".equalsIgnoreCase(isPartialHeader);

        if (isFragmentRequest) {
            return "usuarios/crear-estudiante :: content";
        }

        return "usuarios/crear-estudiante";
    }

    @GetMapping("/facultades")
    public ResponseEntity<List<Facultad>> getFacultades() {
        return ResponseEntity.ok(facultadRepository.findAll());
    }

    @GetMapping("/carreras/facultad/{id}")
    public ResponseEntity<List<Carrera>> getCarrerasPorFacultad(@PathVariable Long id) {
        return ResponseEntity.ok(carreraRepository.findByFacultad_Id(id));
    }

    @GetMapping("/asignaturas/all")
    public ResponseEntity<List<AsignaturaSimpleDTO>> getAsignaturas() {
        return ResponseEntity.ok(asignaturaService.findAllAsSimpleDTO());
    }

    @GetMapping("/convocatorias/all")
    public ResponseEntity<List<Convocatoria>> getConvocatorias() {
        return ResponseEntity.ok(convocatoriaRepository.findAll());
    }
}