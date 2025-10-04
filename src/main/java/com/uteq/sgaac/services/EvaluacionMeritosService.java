package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.*;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.DatosEstudiantilRepository;
import com.uteq.sgaac.repository.EvaluacionMeritosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EvaluacionMeritosService {

    @Autowired
    private EvaluacionMeritosRepository evaluacionMeritosRepository;

    @Autowired
    private DatosEstudiantilRepository datosEstudiantilRepository;

    public EvaluacionMeritos save(EvaluacionMeritos evaluacion) {
        return evaluacionMeritosRepository.save(evaluacion);
    }

    public List<EvaluacionMeritos> findAll() {
        return evaluacionMeritosRepository.findAll();
    }

    public Optional<EvaluacionMeritos> findById(Long id) {
        return evaluacionMeritosRepository.findById(id);
    }

    public void deleteById(Long id) {
        evaluacionMeritosRepository.deleteById(id);
    }

    public List<EvaluacionMeritos> findEvaluacionesPendientes() {
        return evaluacionMeritosRepository.findByEstado("NO_CALCULADO");
    }

    @Transactional(readOnly = true)
    public MeritosDetalleDTO getDetallesParaEvaluacion(Long idEvaluacion) {
        EvaluacionMeritos evaluacion = evaluacionMeritosRepository.findById(idEvaluacion)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación de méritos no encontrada con ID: " + idEvaluacion));

        Postulacion postulacion = evaluacion.getPostulacion();
        Estudiante estudiante = postulacion.getEstudiante();
        Asignatura asignatura = postulacion.getAsignatura();

        // Obtener y mapear los documentos de méritos
        List<DocumentoMeritoDTO> documentosMeritosDTO = postulacion.getDocumentosMerito().stream().map(dm -> {
            CriterioMeritoDTO criterioDTO = new CriterioMeritoDTO(dm.getCriterioMerito().getId(), dm.getCriterioMerito().getDescripcion(), dm.getCriterioMerito().getPuntosMaximos());
            return new DocumentoMeritoDTO(dm.getId(), dm.getRutaArchivo(), criterioDTO);
        }).collect(Collectors.toList());

        // Crear y poblar el DTO
        MeritosDetalleDTO dto = new MeritosDetalleDTO();
        dto.setNombreEstudiante(estudiante.getUsuario().getNombres() + " " + estudiante.getUsuario().getApellidos());
        dto.setNombreAsignatura(asignatura.getNombre());
        dto.setDocumentosMerito(documentosMeritosDTO);

        // Cargar el desglose de puntos si ya fue guardado previamente
        dto.setPuntosCalificacionAsignatura(evaluacion.getPuntosCalificacionAsignatura());
        dto.setPuntosPromedioGeneral(evaluacion.getPuntosPromedioGeneral());
        dto.setPuntosExperiencia(evaluacion.getPuntosExperiencia());
        dto.setPuntosEventos(evaluacion.getPuntosEventos());

        return dto;
    }

    @Transactional
    public EvaluacionMeritos guardarEvaluacion(Long idEvaluacion, GuardarMeritosDTO dto) {
        EvaluacionMeritos evaluacion = evaluacionMeritosRepository.findById(idEvaluacion)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación de méritos no encontrada con ID: " + idEvaluacion));

        // Guardar el desglose de puntos
        evaluacion.setPuntosCalificacionAsignatura(dto.getPuntosCalificacionAsignatura());
        evaluacion.setPuntosPromedioGeneral(dto.getPuntosPromedioGeneral());
        evaluacion.setPuntosExperiencia(dto.getPuntosExperiencia());
        evaluacion.setPuntosEventos(dto.getPuntosEventos());
        evaluacion.setPuntajeTotal(dto.getPuntajeTotal());
        
        evaluacion.setEstado("CALCULADO");

        return evaluacionMeritosRepository.save(evaluacion);
    }
}
