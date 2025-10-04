package com.uteq.sgaac.services;

import com.uteq.sgaac.model.ConvocatoriaAsignaturaRequisito;
import com.uteq.sgaac.repository.ConvocatoriaAsignaturaRequisitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvocatoriaAsignaturaRequisitoService {

    @Autowired
    private ConvocatoriaAsignaturaRequisitoRepository repository;

    public ConvocatoriaAsignaturaRequisito save(ConvocatoriaAsignaturaRequisito entity) {
        return repository.save(entity);
    }

    public List<ConvocatoriaAsignaturaRequisito> saveAll(List<ConvocatoriaAsignaturaRequisito> entities) {
        return repository.saveAll(entities);
    }

    public List<ConvocatoriaAsignaturaRequisito> findAll() {
        return repository.findAll();
    }
}
