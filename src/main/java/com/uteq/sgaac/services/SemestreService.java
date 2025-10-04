package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Semestre;
import com.uteq.sgaac.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemestreService {

    @Autowired
    private SemestreRepository semestreRepository;

    public List<Semestre> findAll() {
        return semestreRepository.findAll();
    }

    public Optional<Semestre> findById(int id) {
        return semestreRepository.findById(id);
    }

    public Semestre save(Semestre semestre) {
        return semestreRepository.save(semestre);
    }

    public void deleteById(int id) {
        semestreRepository.deleteById(id);
    }
}
