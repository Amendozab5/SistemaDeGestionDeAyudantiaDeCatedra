package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AsistenciaDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.AsignaturaRepository;
import com.uteq.sgaac.repository.AsistenciaEstudianteRepository;
import com.uteq.sgaac.repository.AyudanteCatedraRepository;
import com.uteq.sgaac.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaEstudianteRepository asistenciaEstudianteRepository;

    @Autowired
    private AyudanteCatedraRepository ayudanteCatedraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Transactional
    public void guardarAsistencias(List<AsistenciaDTO> asistencias, Long idAyudante, Long idAsignatura, LocalDateTime fechaSesion) {

        AyudanteCatedra ayudante = ayudanteCatedraRepository.findById(idAyudante)
                .orElseThrow(() -> new IllegalArgumentException("Ayudante no encontrado"));

        Asignatura asignatura = asignaturaRepository.findById(idAsignatura)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada"));

        for (AsistenciaDTO dto : asistencias) {
            Estudiante estudiante = estudianteRepository.findById(dto.getIdEstudiante())
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con id: " + dto.getIdEstudiante()));

            AsistenciaEstudiante asistencia = new AsistenciaEstudiante();
            asistencia.setAyudante(ayudante);
            asistencia.setEstudiante(estudiante);
            asistencia.setAsignatura(asignatura);
            asistencia.setFechaSesion(fechaSesion);
            asistencia.setEstado(dto.getEstado());
            asistencia.setObservaciones(dto.getObservaciones());

            asistenciaEstudianteRepository.save(asistencia);
        }
    }
}
