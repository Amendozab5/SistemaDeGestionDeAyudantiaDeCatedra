package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AsignarTribunalDTO;
import com.uteq.sgaac.dto.FinalizacionDetalleDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class PostulacionService {

    private static final Logger logger = LoggerFactory.getLogger(PostulacionService.class);
    private static final BigDecimal UMBRAL_GANADOR = new BigDecimal("25.00");

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository;

    @Autowired
    private ValidacionPostulacionService validacionPostulacionService;

    @Autowired
    private PostulacionDocumentoRepository postulacionDocumentoRepository;

    @Autowired
    private EvaluacionMeritosRepository evaluacionMeritosRepository;

    @Autowired
    private PruebaOposicionRepository pruebaOposicionRepository;

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private DocumentoMeritoRepository documentoMeritoRepository;

    @Autowired
    private CriterioMeritoRepository criterioMeritoRepository;

    @Autowired
    private RequisitoRepository requisitoRepository;

    @Autowired
    private DocenteRepository docenteRepository;



    @Transactional
    public Postulacion crearPostulacion(Long plazaId, String username, java.util.Map<String, MultipartFile> files) {

        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        Estudiante estudiante = estudianteRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalStateException("Estudiante no encontrado"));

        ConvocatoriaAsignatura plaza = convocatoriaAsignaturaRepository.findById(plazaId)
                .orElseThrow(() -> new IllegalArgumentException("Plaza no encontrada"));

        Postulacion postulacion = new Postulacion();
        postulacion.setEstudiante(estudiante);
        postulacion.setAsignatura(plaza.getAsignatura());
        postulacion.setConvocatoria(plaza.getConvocatoria());
        postulacion.setEstadoPostulacion(PostulacionEstado.EN_REVISION);
        postulacion.setFechaPostulacion(LocalDateTime.now());

        Postulacion savedPostulacion = postulacionRepository.save(postulacion);

        if (files != null) {
            for (java.util.Map.Entry<String, MultipartFile> entry : files.entrySet()) {
                String inputName = entry.getKey();
                MultipartFile file = entry.getValue();

                if (file == null || file.isEmpty()) {
                    continue;
                }

                try {
                    if (inputName.startsWith("merito-")) {
                        // Es un documento de mérito
                        Long criterioId = Long.parseLong(inputName.substring("merito-".length()));
                        CriterioMerito criterio = criterioMeritoRepository.findById(criterioId)
                                .orElseThrow(() -> new IllegalArgumentException("Criterio de mérito no encontrado con ID: " + criterioId));

                        String rutaArchivo = storeFile(file, estudiante.getIdEstudiante(), "merito_" + criterioId);

                        DocumentoMerito docMerito = new DocumentoMerito();
                        docMerito.setPostulacion(savedPostulacion);
                        docMerito.setCriterioMerito(criterio);
                        docMerito.setRutaArchivo(rutaArchivo);
                        documentoMeritoRepository.save(docMerito);

                    } else {
                        // Es un documento de requisito
                        Long requisitoId = Long.parseLong(inputName);
                        Requisito requisito = requisitoRepository.findById(requisitoId)
                                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado con ID: " + requisitoId));

                        String rutaArchivo = storeFile(file, estudiante.getIdEstudiante(), "requisito_" + requisitoId);

                        PostulacionDocumento documento = new PostulacionDocumento();
                        documento.setPostulacion(savedPostulacion);
                        documento.setTipoDocumento("requisito_" + requisito.getId());
                        documento.setRutaArchivo(rutaArchivo);
                        documento.setEsValido(false); // Por defecto no es válido hasta revisión
                        documento.setRequisito(requisito);
                        postulacionDocumentoRepository.save(documento);
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid ID format in input name: {}", inputName);
                }
            }
        }

        // Execute asynchronous validation if necessary
        validacionPostulacionService.validarPostulacion(savedPostulacion);

        return savedPostulacion;
    }

    private String storeFile(MultipartFile file, Long estudianteId, String docType) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "";
        }
        String cleanedFilename = StringUtils.cleanPath(originalFilename);
        String filename = estudianteId + "_" + docType + "_" + System.currentTimeMillis() + "_" + cleanedFilename;
        fileStorageService.save(file, filename);
        return filename;
    }


    public Postulacion save(Postulacion postulacion) {
        return postulacionRepository.save(postulacion);
    }

    public List<Postulacion> findAll() {
        return postulacionRepository.findAllWithDocumentos();
    }

    public Optional<Postulacion> findById(Long id) {
        return postulacionRepository.findById(id);
    }

    public void deleteById(Long id) {
        postulacionRepository.deleteById(id);
    }

    @Transactional
    public void asignarTribunal(AsignarTribunalDTO dto) {
        List<Postulacion> postulaciones = postulacionRepository.findAllById(dto.getPostulacionIds());
        if (postulaciones.size() != dto.getPostulacionIds().size()) {
            throw new IllegalArgumentException("Algunas postulaciones no fueron encontradas.");
        }

        Set<Docente> tribunal = new HashSet<>(docenteRepository.findAllById(dto.getTribunalIds()));
        if (tribunal.size() != dto.getTribunalIds().size()) {
            throw new IllegalArgumentException("Algunos docentes del tribunal no fueron encontrados.");
        }

        for (Postulacion postulacion : postulaciones) {
            postulacion.setTribunalAsignado(tribunal);
        }

        postulacionRepository.saveAll(postulaciones);
    }

    @Transactional
    public Postulacion aprobarPostulacion(Long id) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Postulación no encontrada con ID: " + id));

        long documentosSubidos = postulacion.getDocumentos().size();

        long documentosValidados = postulacion.getDocumentos().stream().filter(PostulacionDocumento::isEsValido).count();

        if (documentosSubidos > 0 && documentosSubidos > documentosValidados) {
            throw new IllegalStateException("No se puede aprobar la postulación. No todos los documentos han sido validados.");
        }

        postulacion.setEstadoPostulacion(PostulacionEstado.APROBADO);

        // Crear registro de evaluación de méritos si no existe
        evaluacionMeritosRepository.findByPostulacion(postulacion).orElseGet(() -> {
            EvaluacionMeritos nuevaEvaluacion = new EvaluacionMeritos();
            nuevaEvaluacion.setPostulacion(postulacion);
            nuevaEvaluacion.setEstado("NO_CALCULADO");
            nuevaEvaluacion.setPuntajeTotal(BigDecimal.ZERO);
            return evaluacionMeritosRepository.save(nuevaEvaluacion);
        });

        return postulacionRepository.save(postulacion);
    }

    public Postulacion rechazarPostulacion(Long id, String motivo) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Postulación no encontrada con ID: " + id));
        postulacion.setEstadoPostulacion(PostulacionEstado.RECHAZADO);
        postulacion.setMotivoRechazo(motivo);
        return postulacionRepository.save(postulacion);
    }

    @Transactional
    public void actualizarEstadoDocumento(Long postulacionId, String tipoDocumento, boolean esValido) {
        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new IllegalArgumentException("Postulación no encontrada con ID: " + postulacionId));

        PostulacionDocumento documento = postulacionDocumentoRepository
                .findByPostulacionAndTipoDocumento(postulacion, tipoDocumento)
                .orElse(new PostulacionDocumento(postulacion, tipoDocumento, esValido, BigDecimal.ZERO, null));

        documento.setEsValido(esValido);
        postulacionDocumentoRepository.save(documento);
    }

    @Transactional(readOnly = true)
    public FinalizacionDetalleDTO getDetallesFinalizacion(Long postulacionId) {
        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new IllegalArgumentException("Postulación no encontrada con ID: " + postulacionId));

        Estudiante estudiante = postulacion.getEstudiante();
        Asignatura asignatura = postulacion.getAsignatura();

        // Buscar puntaje de méritos
        BigDecimal puntajeMeritos = evaluacionMeritosRepository.findByPostulacion(postulacion)
                .map(EvaluacionMeritos::getPuntajeTotal)
                .orElse(BigDecimal.ZERO);

        // Buscar puntaje de oposición
        BigDecimal puntajeOposicion = pruebaOposicionRepository.findByPostulacionId(postulacionId)
                .map(PruebaOposicion::getPuntajeTotal)
                .orElse(BigDecimal.ZERO);

        // Calcular total
        BigDecimal puntajeTotal = puntajeMeritos.add(puntajeOposicion);

        // Crear y poblar DTO
        FinalizacionDetalleDTO dto = new FinalizacionDetalleDTO();
        dto.setNombreEstudiante(estudiante.getUsuario().getNombres() + " " + estudiante.getUsuario().getApellidos());
        dto.setNombreAsignatura(asignatura.getNombre());
        dto.setPuntajeMeritos(puntajeMeritos);
        dto.setPuntajeOposicion(puntajeOposicion);
        dto.setPuntajeTotal(puntajeTotal);

        return dto;
    }

    @Autowired
    private AyudanteCatedraRepository ayudanteCatedraRepository;

    @Transactional
    public void finalizarPostulacion(Long postulacionId) {
        logger.info("Iniciando proceso de finalización para la postulación ID: {}", postulacionId);

        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new IllegalStateException("Postulación no encontrada con ID: " + postulacionId));

        // 1. Obtener la nota de méritos
        EvaluacionMeritos meritos = evaluacionMeritosRepository.findByPostulacion(postulacion)
                .orElseThrow(() -> new IllegalStateException("No se encontró la evaluación de méritos para la postulación: " + postulacionId));
        BigDecimal puntajeMeritos = meritos.getPuntajeTotal();
        logger.info("Puntaje de méritos para postulación {}: {}", postulacionId, puntajeMeritos);

        // 2. Obtener la nota de oposición
        PruebaOposicion oposicion = pruebaOposicionRepository.findByPostulacion(postulacion)
                .orElseThrow(() -> new IllegalStateException("No se encontró la prueba de oposición para la postulación: " + postulacionId));
        BigDecimal puntajeOposicion = oposicion.getPuntajeTotal();
        logger.info("Puntaje de oposición para postulación {}: {}", postulacionId, puntajeOposicion);

        // 3. Calcular el puntaje final (Suma simple)
        BigDecimal puntajeFinal = puntajeMeritos.add(puntajeOposicion);
        logger.info("Puntaje final calculado para postulación {}: {}", postulacionId, puntajeFinal);

        // 4. Crear o actualizar el resultado
        Resultado resultado = postulacion.getResultado();
        if (resultado == null) {
            resultado = new Resultado(postulacion);
            postulacion.setResultado(resultado);
        }

        resultado.setPuntajeTotalFinal(puntajeFinal);

        // 5. Determinar el estado final
        if (puntajeFinal.compareTo(UMBRAL_GANADOR) >= 0) {
            resultado.setEstadoFinal("GANADOR");
            logger.info("DEBUG: Postulación {} marcada como GANADOR.", postulacionId);
        } else {
            resultado.setEstadoFinal("PERDEDOR");
            logger.info("DEBUG: Postulación {} marcada como PERDEDOR.", postulacionId);
        }
        
        resultadoRepository.save(resultado);
        logger.info("Estado final para postulación {}: {}", postulacionId, resultado.getEstadoFinal());

        // 6. Actualizar estado de la Prueba de Oposición
        oposicion.setEstado("FINALIZADA");
        logger.info("Estado de la prueba de oposición para postulación {} actualizado a FINALIZADA.", postulacionId);

        // 7. Actualizar estado de la postulación
        postulacion.setEstadoPostulacion(PostulacionEstado.CALIFICADO);
        logger.info("Estado de la postulación {} actualizado a CALIFICADO.", postulacionId);

    }

    @Transactional
    public void convertirGanadorEnAyudante(Long postulacionId) {
        logger.info("Iniciando proceso de conversión a ayudante para la postulación ID: {}", postulacionId);

        Postulacion postulacion = postulacionRepository.findById(postulacionId)
                .orElseThrow(() -> new IllegalStateException("Postulación no encontrada con ID: " + postulacionId));

        // Verificar que el estado sea GANADOR
        if (postulacion.getResultado() == null || !"GANADOR".equals(postulacion.getResultado().getEstadoFinal())) {
            throw new IllegalStateException("La postulación no tiene el estado 'GANADOR'.");
        }

        // Verificar que no haya sido convertido ya
        if (ayudanteCatedraRepository.findByPostulacion(postulacion).isPresent()) {
            throw new IllegalStateException("Este postulante ya ha sido convertido en Ayudante de Cátedra.");
        }

        // Crear y guardar el registro de AyudanteCatedra
        AyudanteCatedra nuevoAyudante = new AyudanteCatedra();
        nuevoAyudante.setPostulacion(postulacion);
        nuevoAyudante.setEstudiante(postulacion.getEstudiante());
        nuevoAyudante.setUsuario(postulacion.getEstudiante().getUsuario());
        nuevoAyudante.setFechaInicio(java.time.LocalDate.now());
        nuevoAyudante.setEstado(AyudantiaEstado.VIGENTE);
        ayudanteCatedraRepository.save(nuevoAyudante);
        logger.info("Registro de Ayudante de Cátedra creado para el estudiante ID: {}", postulacion.getEstudiante().getIdEstudiante());

        // Cambiar el rol del usuario
        roles rolAyudante = rolRepository.findByNombre("AYUDANTE")
                .orElseThrow(() -> new IllegalStateException("El rol 'AYUDANTE' no se encuentra en la base de datos."));
        
        usuario_sistema usuario = postulacion.getEstudiante().getUsuario();
        usuario.setRol(rolAyudante);
        usuarioRepository.save(usuario);
        logger.info("Rol del usuario {} actualizado a AYUDANTE.", usuario.getUsername());
        
        // Actualizar estado de la postulación para que no aparezca más en la lista
        postulacion.setEstadoPostulacion(PostulacionEstado.FINALIZADO);
        postulacionRepository.save(postulacion);
        logger.info("Estado de la postulación {} actualizado a FINALIZADO.", postulacionId);
    }
}
