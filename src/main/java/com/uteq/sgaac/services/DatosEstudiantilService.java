package com.uteq.sgaac.services;

import com.uteq.sgaac.model.DatosEstudiantil;
import com.uteq.sgaac.model.DatosEstudiantilId;
import com.uteq.sgaac.repository.DatosEstudiantilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DatosEstudiantilService {

    @Autowired
    private DatosEstudiantilRepository datosEstudiantilRepository;

    public DatosEstudiantil save(DatosEstudiantil datos) {
        return datosEstudiantilRepository.save(datos);
    }

    public List<DatosEstudiantil> findAll() {
        return datosEstudiantilRepository.findAll();
    }

    public Optional<DatosEstudiantil> findById(Long estudianteId, Long asignaturaId) {
        DatosEstudiantilId id = new DatosEstudiantilId();
        id.setEstudiante(estudianteId);
        id.setAsignatura(asignaturaId);
        return datosEstudiantilRepository.findById(id);
    }

    public void deleteById(Long estudianteId, Long asignaturaId) {
        DatosEstudiantilId id = new DatosEstudiantilId();
        id.setEstudiante(estudianteId);
        id.setAsignatura(asignaturaId);
        datosEstudiantilRepository.deleteById(id);
    }
}
