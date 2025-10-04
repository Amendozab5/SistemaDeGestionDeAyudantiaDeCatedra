package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Coordinador;
import com.uteq.sgaac.repository.CoordinadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordinadorService {

    @Autowired
    private CoordinadorRepository coordinadorRepository;

    public Coordinador save(Coordinador coordinador) {
        return coordinadorRepository.save(coordinador);
    }

    public List<Coordinador> findAll() {
        return coordinadorRepository.findAll();
    }

    public Optional<Coordinador> findById(Long id) {
        return coordinadorRepository.findById(id);
    }

    public void deleteById(Long id) {
        coordinadorRepository.deleteById(id);
    }
}
