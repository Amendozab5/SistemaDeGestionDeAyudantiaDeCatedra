package com.uteq.sgaac.services;

import com.uteq.sgaac.model.ConvocatoriaAsignatura;
import com.uteq.sgaac.repository.ConvocatoriaAsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConvocatoriaAsignaturaService {

    @Autowired
    private ConvocatoriaAsignaturaRepository convocatoriaAsignaturaRepository;

    public ConvocatoriaAsignatura save(ConvocatoriaAsignatura convocatoriaAsignatura) {
        return convocatoriaAsignaturaRepository.save(convocatoriaAsignatura);
    }

    public List<ConvocatoriaAsignatura> findAll() {
        return convocatoriaAsignaturaRepository.findAll();
    }

    public Optional<ConvocatoriaAsignatura> findById(Long id) {
        return convocatoriaAsignaturaRepository.findById(id);
    }

    public void deleteById(Long id) {
        convocatoriaAsignaturaRepository.deleteById(id);
    }
}