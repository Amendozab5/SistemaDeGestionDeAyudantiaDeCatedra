package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Facultad;
import com.uteq.sgaac.repository.FacultadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultadService {

    @Autowired
    private FacultadRepository facultadRepository;

    public List<Facultad> findAll() {
        return facultadRepository.findAll();
    }

    public Facultad save(Facultad facultad) {
        return facultadRepository.save(facultad);
    }

    public void deleteById(Long id) {
        facultadRepository.deleteById(id);
    }

    public Facultad findById(Long id) {
        return facultadRepository.findById(id).orElse(null);
    }
}
