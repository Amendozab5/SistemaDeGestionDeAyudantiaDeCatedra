package com.uteq.sgaac.services;

import com.uteq.sgaac.model.CriterioMerito;
import com.uteq.sgaac.repository.CriterioMeritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CriterioMeritoService {

    @Autowired
    private CriterioMeritoRepository criterioMeritoRepository;

    @Transactional(readOnly = true)
    public List<CriterioMerito> findAll() {
        return criterioMeritoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CriterioMerito> findById(Long id) {
        return criterioMeritoRepository.findById(id);
    }

    @Transactional
    public CriterioMerito save(CriterioMerito criterio) {
        return criterioMeritoRepository.save(criterio);
    }

    @Transactional
    public void deleteById(Long id) {
        criterioMeritoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CriterioMerito> findByActivoTrue() {
        return criterioMeritoRepository.findByActivoTrue();
    }
}
