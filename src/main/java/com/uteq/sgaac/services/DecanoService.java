package com.uteq.sgaac.services;

import com.uteq.sgaac.model.Decano;
import com.uteq.sgaac.repository.DecanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DecanoService {

    @Autowired
    private DecanoRepository decanoRepository;

    public Decano save(Decano decano) {
        return decanoRepository.save(decano);
    }

    public List<Decano> findAll() {
        return decanoRepository.findAll();
    }

    public Optional<Decano> findById(Long id) {
        return decanoRepository.findById(id);
    }

    public void deleteById(Long id) {
        decanoRepository.deleteById(id);
    }
}
