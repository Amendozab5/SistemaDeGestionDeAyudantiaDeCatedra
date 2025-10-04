package com.uteq.sgaac.services;

import com.uteq.sgaac.model.ActividadAyudante;
import com.uteq.sgaac.repository.ActividadAyudanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadAyudanteService {

    @Autowired
    private ActividadAyudanteRepository actividadAyudanteRepository;

    public ActividadAyudante save(ActividadAyudante actividad) {
        return actividadAyudanteRepository.save(actividad);
    }

    public List<ActividadAyudante> findAll() {
        return actividadAyudanteRepository.findAll();
    }

    public Optional<ActividadAyudante> findById(Long id) {
        return actividadAyudanteRepository.findById(id);
    }

    public void deleteById(Long id) {
        actividadAyudanteRepository.deleteById(id);
    }
}
