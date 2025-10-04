package com.uteq.sgaac.services;

import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

@Service
public class PruebaOposicionEvalService {

    private static final Logger logger = LoggerFactory.getLogger(PruebaOposicionEvalService.class);

    private final PruebaOposicionEvalRepository pruebaOposicionEvalRepository;
    private final PruebaOposicionRepository pruebaOposicionRepository;
    private final DocenteRepository docenteRepository;
    private final PruebaOposicionTribunalRepository pruebaOposicionTribunalRepository;
    private final EvaluacionMeritosRepository evaluacionMeritosRepository;
    private final ResultadoRepository resultadoRepository;
    private final AyudanteCatedraRepository ayudanteCatedraRepository;
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    private final RolRepository rolRepository;
    private final EntityManager entityManager; // Added EntityManager


    private static final BigDecimal UMBRAL_APROBACION = new BigDecimal("14.00"); // Aprobación con 14/20
    private static final BigDecimal UMBRAL_GANADOR = new BigDecimal("70.00");

    public PruebaOposicionEvalService(PruebaOposicionEvalRepository pruebaOposicionEvalRepository, PruebaOposicionRepository pruebaOposicionRepository, DocenteRepository docenteRepository, PruebaOposicionTribunalRepository pruebaOposicionTribunalRepository, EvaluacionMeritosRepository evaluacionMeritosRepository, ResultadoRepository resultadoRepository, AyudanteCatedraRepository ayudanteCatedraRepository, UsuarioSistemaRepository usuarioSistemaRepository, RolRepository rolRepository, EntityManager entityManager) { // Added EntityManager
        this.pruebaOposicionEvalRepository = pruebaOposicionEvalRepository;
        this.pruebaOposicionRepository = pruebaOposicionRepository;
        this.docenteRepository = docenteRepository;
        this.pruebaOposicionTribunalRepository = pruebaOposicionTribunalRepository;
        this.evaluacionMeritosRepository = evaluacionMeritosRepository;
        this.resultadoRepository = resultadoRepository;
        this.ayudanteCatedraRepository = ayudanteCatedraRepository;
        this.usuarioSistemaRepository = usuarioSistemaRepository;
        this.rolRepository = rolRepository;
        this.entityManager = entityManager; // Added EntityManager
    }

    @Transactional
    public PruebaOposicionEval guardarCalificacionIndividual(PruebaOposicionEval evaluacion) {
        // 1. Guardar la evaluación individual
        PruebaOposicionEval savedEval = pruebaOposicionEvalRepository.save(evaluacion);
        PruebaOposicion prueba = savedEval.getOposicion();

        // 2. Recalcular el promedio
        List<PruebaOposicionEval> allEvals = pruebaOposicionEvalRepository.findByOposicion(prueba);
        BigDecimal sum = allEvals.stream()
                .map(PruebaOposicionEval::getPuntajeTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = BigDecimal.ZERO;
        if (!allEvals.isEmpty()) {
            average = sum.divide(new BigDecimal(allEvals.size()), 2, RoundingMode.HALF_UP);
        }

        // 3. Actualizar el objeto PruebaOposicion principal
        prueba.setPuntajeTotal(average);

        // 4. Comprobar si todos los miembros del tribunal han evaluado
        long totalTribunales = pruebaOposicionTribunalRepository.countByPruebaOposicion(prueba);
        if (allEvals.size() >= totalTribunales) {
            prueba.setEstado("PENDIENTE_CIERRE");
            if (average.compareTo(UMBRAL_APROBACION) >= 0) {
                prueba.setResultado("APROBADO");
            } else {
                prueba.setResultado("REPROBADO");
            }
            pruebaOposicionRepository.save(prueba);
            // Si todos han evaluado, calcular el resultado final
            calcularYGuardarResultadoFinal(prueba, average);
        } else {
            pruebaOposicionRepository.save(prueba);
        }

        return savedEval;
    }


    @Transactional
    public void guardarYCalcularResultados(Long idPruebaOposicion, List<PruebaOposicionEval> evaluaciones) {
        logger.info("Iniciando el proceso de guardar y calcular resultados para la prueba de oposición ID: {}", idPruebaOposicion);
        PruebaOposicion prueba = pruebaOposicionRepository.findById(idPruebaOposicion)
                .orElseThrow(() -> new IllegalArgumentException("Prueba de oposición no encontrada con ID: " + idPruebaOposicion));

        // Limpiar evaluaciones anteriores para esta prueba
        pruebaOposicionEvalRepository.deleteByOposicion(prueba);
        logger.info("Evaluaciones anteriores para la prueba ID: {} han sido eliminadas.", idPruebaOposicion);

        BigDecimal puntajeSumaTotal = BigDecimal.ZERO;
        int numeroEvaluadores = 0;

        for (PruebaOposicionEval eval : evaluaciones) {
            // Asegurarse de que el docente está cargado y asignado
            Docente docente = docenteRepository.findById(eval.getDocente().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
            eval.setDocente(docente);
            eval.setOposicion(prueba);

            BigDecimal puntajeIndividual = eval.getMaterialPuntos().add(eval.getCalidadPuntos()).add(eval.getRespuestasPuntos());
            
            pruebaOposicionEvalRepository.save(eval);
            puntajeSumaTotal = puntajeSumaTotal.add(puntajeIndividual);
            numeroEvaluadores++;
        }
        logger.info("Se guardaron {} nuevas evaluaciones.", numeroEvaluadores);

        BigDecimal puntajePromedioOposicion = BigDecimal.ZERO;
        if (numeroEvaluadores > 0) {
            puntajePromedioOposicion = puntajeSumaTotal.divide(new BigDecimal(numeroEvaluadores), 2, RoundingMode.HALF_UP);
        }
        logger.info("Puntaje promedio de oposición calculado: {}", puntajePromedioOposicion);

        prueba.setPuntajeTotal(puntajePromedioOposicion);
        prueba.setEstado("PENDIENTE_CIERRE");

        if (puntajePromedioOposicion.compareTo(UMBRAL_APROBACION) >= 0) {
            prueba.setResultado("APROBADO");
        } else {
            prueba.setResultado("REPROBADO");
        }

        pruebaOposicionRepository.save(prueba);
        logger.info("Prueba de oposición ID: {} actualizada con puntaje y estado.", idPruebaOposicion);

        // --- INICIO DE LA NUEVA LÓGICA ---
        calcularYGuardarResultadoFinal(prueba, puntajePromedioOposicion);
        // --- FIN DE LA NUEVA LÓGICA ---
    }

    public Optional<PruebaOposicionEval> findById(Long id) {
        return pruebaOposicionEvalRepository.findById(id);
    }

    private void calcularYGuardarResultadoFinal(PruebaOposicion prueba, BigDecimal puntajePromedioOposicion) {
        try {
            logger.info("Iniciando cálculo de resultado final para la postulación ID: {}", prueba.getPostulacion().getIdPostulacion());
            // 1. Obtener la postulación
            Postulacion postulacion = prueba.getPostulacion();

            // 2. Obtener la nota de méritos
            Optional<EvaluacionMeritos> meritosOpt = evaluacionMeritosRepository.findByPostulacion(postulacion);
            if (meritosOpt.isEmpty()) {
                logger.error("No se encontró la evaluación de méritos para la postulación: {}", postulacion.getIdPostulacion());
                throw new IllegalStateException("No se encontró la evaluación de méritos para la postulación: " + postulacion.getIdPostulacion());
            }
            EvaluacionMeritos meritos = meritosOpt.get();
            BigDecimal puntajeMeritos = meritos.getPuntajeTotal();
            logger.info("Puntaje de méritos obtenido: {}", puntajeMeritos);

            // 3. Calcular el puntaje final (Meritos vale 60, Oposición 40)
            BigDecimal puntajeFinal = (puntajeMeritos.multiply(new BigDecimal("0.6")))
                    .add(puntajePromedioOposicion.multiply(new BigDecimal("0.4")));
            logger.info("Puntaje final calculado: {}", puntajeFinal);

            // 4. Guardar en la tabla de resultados
            // Usar bloqueo pesimista para evitar StaleObjectStateException
            Resultado resultado;
            Optional<Resultado> resultadoOpt = resultadoRepository.findByPostulacion(postulacion);

            if (resultadoOpt.isPresent()) {
                resultado = entityManager.find(Resultado.class, resultadoOpt.get().getId(), LockModeType.PESSIMISTIC_WRITE);
                resultado.setPuntajeTotalFinal(puntajeFinal);
            } else {
                resultado = new Resultado(postulacion);
                resultado.setPuntajeTotalFinal(puntajeFinal);
            }

            // 5. Determinar el estado final
            if (puntajeFinal.compareTo(UMBRAL_GANADOR) >= 0) {
                resultado.setEstadoFinal("GANADOR");
                logger.info("¡Postulante {} es GANADOR!", postulacion.getEstudiante().getUsuario().getNombres());

                // --- Lógica para crear Ayudante de Cátedra y actualizar rol ---
                Estudiante estudiante = postulacion.getEstudiante();
                usuario_sistema usuario = estudiante.getUsuario();

                // Verificar si ya existe una ayudantía para esta postulación
                Optional<AyudanteCatedra> ayudantiaExistente = ayudanteCatedraRepository.findByPostulacion(postulacion);
                if (ayudantiaExistente.isEmpty()) {
                    // Crear y guardar la nueva ayudantía
                    AyudanteCatedra nuevaAyudantia = new AyudanteCatedra();
                    nuevaAyudantia.setPostulacion(postulacion);
                    nuevaAyudantia.setEstudiante(estudiante);
                    nuevaAyudantia.setUsuario(usuario);
                    // El estado y fecha de inicio se establecen por defecto en la entidad
                    ayudanteCatedraRepository.save(nuevaAyudantia);
                    ayudanteCatedraRepository.flush();
                    logger.info("Se ha creado una nueva entrada en AyudanteCatedra para el estudiante ID: {}", estudiante.getIdEstudiante());

                    // Actualizar el rol del usuario
                    roles rolAyudante = rolRepository.findByNombre("ESTUDIANTE_AYUDANTE")
                            .orElseThrow(() -> new IllegalStateException("El rol 'ESTUDIANTE_AYUDANTE' no se encuentra en la base de datos."));
                    usuario.setRol(rolAyudante);
                    usuarioSistemaRepository.save(usuario);
                    logger.info("Se ha actualizado el rol del usuario ID: {} a ESTUDIANTE_AYUDANTE", usuario.getIdUsuario());
                } else {
                    logger.warn("Ya existe una ayudantía para la postulación ID: {}. No se creará una nueva.", postulacion.getIdPostulacion());
                }
                // --- Fin de la lógica ---

            } else {
                resultado.setEstadoFinal("PERDEDOR");
            }
            logger.info("Estado final determinado: {}", resultado.getEstadoFinal());

            resultadoRepository.save(resultado);
            logger.info("Resultado para la postulación ID: {} guardado exitosamente en la base de datos.", postulacion.getIdPostulacion());

        } catch (Exception e) {
            logger.error("Ocurrió un error al calcular y guardar el resultado final para la postulación ID: {}. Error: {}", prueba.getPostulacion().getIdPostulacion(), e.getMessage(), e);
            // Relanzamos la excepción para asegurar la consistencia de los datos
            throw e;
        }
    }
}
