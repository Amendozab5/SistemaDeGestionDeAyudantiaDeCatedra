package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.repository.CarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    public List<Carrera> findAll() {
        return carreraRepository.findAll();
    }

    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public void deleteById(Long id) {
        carreraRepository.deleteById(id);
    }

    public Carrera findById(Long id) {
        return carreraRepository.findById(id).orElse(null);
    }

    public List<Carrera> findByFacultad_Id(Long id) {
        return carreraRepository.findByFacultad_Id(id);
    }
}
