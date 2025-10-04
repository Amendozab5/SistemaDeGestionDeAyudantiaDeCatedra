package com.uteq.sgaac.services;

import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
//import java.util.stream.Collectors;

@Service
public class ValidacionPostulacionService {

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private DatosEstudiantilRepository datosEstudiantilRepository;

    @Autowired
    private SancionRepository sancionRepository;

    @Autowired
    private PostulacionRequisitoRepository postulacionRequisitoRepository;

    @Autowired
    private ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository;


    public void validarPostulacion(Postulacion postulacion) {

        ConvocatoriaAsignatura convocatoriaAsignatura = convocatoriaAsignaturaRepository.findByConvocatoriaAndAsignatura(
                postulacion.getConvocatoria(), postulacion.getAsignatura())
                .orElseThrow(() -> new IllegalStateException("No se encontró la ConvocatoriaAsignatura para la postulación"));

        Set<Requisito> requisitos = convocatoriaAsignatura.getRequisitos();

        boolean todosRequisitosCumplidos = true;

        for (Requisito requisito : requisitos) {
            boolean cumplido = false;

            switch (requisito.getDescripcion()) {
                case "Ser estudiante regular":
                    cumplido = validarEstudianteRegular(postulacion);
                    break;
                case "No tener sanciones":
                    cumplido = !validarSanciones(postulacion);
                    break;
                case "Calificación mínima":
                    cumplido = validarCalificacion(postulacion);
                    break;
                case "Promedio general superior al de la carrera":
                    cumplido = validarPromedioGeneral(postulacion);
                    break;
            }

            PostulacionRequisitoId postulacionRequisitoId = new PostulacionRequisitoId(postulacion.getIdPostulacion(), requisito.getId());
            PostulacionRequisito postulacionRequisito = new PostulacionRequisito();
            postulacionRequisito.setId(postulacionRequisitoId);
            postulacionRequisito.setPostulacion(postulacion);
            postulacionRequisito.setRequisito(requisito);
            postulacionRequisito.setCumplido(cumplido);
            postulacionRequisitoRepository.save(postulacionRequisito);

            if (!cumplido) {
                todosRequisitosCumplidos = false;
            }
        }

        if (!todosRequisitosCumplidos) {
            postulacion.setEstadoPostulacion(PostulacionEstado.RECHAZADO);
        } else {
            postulacion.setEstadoPostulacion(PostulacionEstado.EN_REVISION);
        }

        postulacionRepository.save(postulacion);
    }

    private boolean validarEstudianteRegular(Postulacion postulacion) {
        Estudiante estudiante = postulacion.getEstudiante();
        return estudiante.isEsRegular();
    }

    private boolean validarSanciones(Postulacion postulacion) {
        Long estudianteId = postulacion.getEstudiante().getIdEstudiante();
        List<Sancion> sancionesActivas = sancionRepository.findByEstudianteIdEstudianteAndActiva(estudianteId, true);
        return !sancionesActivas.isEmpty();
    }

    private boolean validarCalificacion(Postulacion postulacion) {
        BigDecimal calificacionMinima = new BigDecimal("8.0"); // Requisito: 8/10
        BigDecimal calificacionEstudiante;
        BigDecimal promedioMateria;

        Estudiante estudiante = postulacion.getEstudiante();
        Asignatura asignatura = postulacion.getAsignatura();
        Convocatoria convocatoria = postulacion.getConvocatoria();
        String periodoConvocatoria = convocatoria.getPeriodo();

        DatosEstudiantilId datosEstudiantilId = new DatosEstudiantilId();
        datosEstudiantilId.setEstudiante(estudiante.getIdEstudiante());
        datosEstudiantilId.setAsignatura(asignatura.getIdAsignatura());

        Optional<DatosEstudiantil> datosEstudiantilOpt = datosEstudiantilRepository.findById(datosEstudiantilId);

        if (datosEstudiantilOpt.isEmpty() || !datosEstudiantilOpt.get().getPeriodo().equals(periodoConvocatoria)) {
            return false;
        }
        calificacionEstudiante = datosEstudiantilOpt.get().getCalificaciones();

        promedioMateria = datosEstudiantilRepository.findAverageCalificacionByAsignaturaAndPeriodo(
            asignatura.getIdAsignatura(), periodoConvocatoria
        );

        if (promedioMateria == null) {
            return false;
        }

        boolean cumpleMinimo = calificacionEstudiante.compareTo(calificacionMinima) >= 0;
        boolean cumplePromedio = calificacionEstudiante.compareTo(promedioMateria) > 0;

        return cumpleMinimo && cumplePromedio;
    }

    private boolean validarPromedioGeneral(Postulacion postulacion) {
        Estudiante estudiante = postulacion.getEstudiante();
        BigDecimal promedioEstudiante = estudiante.getPromedioGeneral();
        Carrera carrera = estudiante.getCarrera();

        if (promedioEstudiante == null) {
            return false;
        }

        BigDecimal promedioCarrera = estudianteRepository.findAveragePromedioGeneralByCarrera(carrera.getId());

        if (promedioCarrera == null) {
            return false;
        }

        return promedioEstudiante.compareTo(promedioCarrera) > 0;
    }
}
