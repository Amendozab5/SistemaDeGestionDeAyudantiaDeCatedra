package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AsignaturaCupoDto;
import com.uteq.sgaac.dto.PlazaEstudianteDTO;
import com.uteq.sgaac.dto.PlazaFormDto;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlazaServiceImpl implements PlazaService {

    private static final Logger logger = LoggerFactory.getLogger(PlazaServiceImpl.class);

    private final ConvocatoriaRepository convocatoriaRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final RequisitoRepository requisitoRepository;
    private final DocenteRepository docenteRepository;
    private final ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository;
    private final ConvocatoriaAsignaturaRequisitoRepository convocatoriaAsignaturaRequisitoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final EstudianteRepository estudianteRepository;
    private final DatosEstudiantilRepository datosEstudiantilRepository;
    private final PostulacionRepository postulacionRepository;

    public PlazaServiceImpl(ConvocatoriaRepository convocatoriaRepository, 
                            AsignaturaRepository asignaturaRepository, 
                            RequisitoRepository requisitoRepository, 
                            DocenteRepository docenteRepository, 
                            ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository, 
                            ConvocatoriaAsignaturaRequisitoRepository convocatoriaAsignaturaRequisitoRepository,
                            UsuarioRepository usuarioRepository,
                            CoordinadorRepository coordinadorRepository,
                            EstudianteRepository estudianteRepository,
                            DatosEstudiantilRepository datosEstudiantilRepository,
                            PostulacionRepository postulacionRepository) {
        this.convocatoriaRepository = convocatoriaRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.requisitoRepository = requisitoRepository;
        this.docenteRepository = docenteRepository;
        this.convocatoriaAsignaturaRepository = convocatoriaAsignaturaRepository;
        this.convocatoriaAsignaturaRequisitoRepository = convocatoriaAsignaturaRequisitoRepository;
        this.usuarioRepository = usuarioRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.estudianteRepository = estudianteRepository;
        this.datosEstudiantilRepository = datosEstudiantilRepository;
        this.postulacionRepository = postulacionRepository;
    }

    @Override
    @Transactional
    public void createPlazas(PlazaFormDto formDto) {
        logger.info("IDs de requisitos recibidos del formulario: {}", formDto.getRequisitoIds());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        usuario_sistema usuario = usuarioRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado para el username: " + currentUsername));
        Coordinador coordinador = coordinadorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalStateException("Coordinador no encontrado para el usuario: " + currentUsername));

        Convocatoria convocatoria = convocatoriaRepository.findById(formDto.getConvocatoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Convocatoria no encontrada con ID: " + formDto.getConvocatoriaId()));

        List<Requisito> requisitos = null;
        if (formDto.getRequisitoIds() != null && !formDto.getRequisitoIds().isEmpty()) {
            requisitos = requisitoRepository.findAllById(formDto.getRequisitoIds());
        }

        logger.info("Número de entidades de Requisito encontradas: {}", (requisitos != null) ? requisitos.size() : 0);

        for (AsignaturaCupoDto asignaturaDto : formDto.getAsignaturas()) {
            Asignatura asignatura = asignaturaRepository.findById(asignaturaDto.getAsignaturaId())
                    .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada con ID: " + asignaturaDto.getAsignaturaId()));

            boolean alreadyExists = convocatoriaAsignaturaRepository.existsByConvocatoriaAndAsignatura(convocatoria, asignatura);

            if (!alreadyExists) {
                ConvocatoriaAsignatura nuevaPlaza = new ConvocatoriaAsignatura();
                nuevaPlaza.setConvocatoria(convocatoria);
                nuevaPlaza.setAsignatura(asignatura);
                nuevaPlaza.setCupos(asignaturaDto.getCupos());
                nuevaPlaza.setCoordinador(coordinador);
                nuevaPlaza.setFechaInicioPostulacion(asignaturaDto.getFechaInicioPostulacion());
                nuevaPlaza.setFechaFinPostulacion(asignaturaDto.getFechaFinPostulacion());

                if (asignaturaDto.getDocenteId() != null) {
                    Docente docente = docenteRepository.findById(asignaturaDto.getDocenteId())
                            .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con ID: " + asignaturaDto.getDocenteId()));
                    nuevaPlaza.setDocente(docente);
                }
                
                ConvocatoriaAsignatura plazaGuardada = convocatoriaAsignaturaRepository.save(nuevaPlaza);
                logger.info("Plaza guardada con ID: {}", plazaGuardada.getId());

                if (requisitos != null && !requisitos.isEmpty()) {
                    logger.info("Asociando {} requisitos a la plaza ID: {}", requisitos.size(), plazaGuardada.getId());
                    for (Requisito requisito : requisitos) {
                        ConvocatoriaAsignaturaRequisitoId requisitoId = new ConvocatoriaAsignaturaRequisitoId(plazaGuardada.getId(), requisito.getId());
                        ConvocatoriaAsignaturaRequisito relacionRequisito = new ConvocatoriaAsignaturaRequisito();
                        relacionRequisito.setId(requisitoId);
                        relacionRequisito.setConvocatoriaAsignatura(plazaGuardada);
                        relacionRequisito.setRequisito(requisito);
                        convocatoriaAsignaturaRequisitoRepository.save(relacionRequisito);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, List<PlazaEstudianteDTO>> findPlazasParaEstudiante(usuario_sistema usuario) {
        java.util.Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuario(usuario);
        if (estudianteOpt.isEmpty()) {
            return Map.of(); // Si no hay perfil de estudiante, no hay plazas para mostrar
        }
        Estudiante estudiante = estudianteOpt.get();

        Carrera carrera = estudiante.getCarrera();
        if (carrera == null) {
            return Map.of(); // Si el estudiante no tiene carrera, no puede postular a nada
        }

        // 1. Get all active plazas for the student's career
        List<ConvocatoriaAsignatura> plazasPorCarrera = convocatoriaAsignaturaRepository.findPlazasActivasPorCarrera(carrera, LocalDate.now());

        // 2. Get all student's applications
        List<Postulacion> postulaciones = postulacionRepository.findByEstudiante(estudiante);
        Set<String> plazasPostuladas = postulaciones.stream()
                .map(p -> p.getConvocatoria().getIdConvocatoria() + "-" + p.getAsignatura().getIdAsignatura())
                .collect(Collectors.toSet());

        // 3. Filter out plazas already applied to and map to DTO
        return plazasPorCarrera.stream()
                .filter(plaza -> !plazasPostuladas.contains(plaza.getConvocatoria().getIdConvocatoria() + "-" + plaza.getAsignatura().getIdAsignatura()))
                .map(plaza -> {
                    String docenteNombre = "N/A";
                    if (plaza.getDocente() != null && plaza.getDocente().getUsuario() != null) {
                        docenteNombre = plaza.getDocente().getUsuario().getNombres() + " " + plaza.getDocente().getUsuario().getApellidos();
                    }
                    return new PlazaEstudianteDTO(
                            plaza.getId(),
                            plaza.getAsignatura().getNombre(),
                            docenteNombre,
                            plaza.getCupos(),
                            plaza.getAsignatura().getCarrera().getCarrera(),
                            plaza.getConvocatoria().getPeriodo(),
                            plaza.getConvocatoria().getTitulo()
                    );
                })
                .collect(Collectors.groupingBy(PlazaEstudianteDTO::getConvocatoriaTitulo));
    }
}
