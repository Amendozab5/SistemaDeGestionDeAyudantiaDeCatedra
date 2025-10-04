package com.uteq.sgaac.services;

import com.uteq.sgaac.model.RequisitoCumplido;
import com.uteq.sgaac.repository.RequisitoCumplidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequisitoCumplidoService {

    @Autowired
    private RequisitoCumplidoRepository requisitoCumplidoRepository;

    public RequisitoCumplido save(RequisitoCumplido requisitoCumplido) {
        return requisitoCumplidoRepository.save(requisitoCumplido);
    }

    public List<RequisitoCumplido> findAll() {
        return requisitoCumplidoRepository.findAll();
    }

    public Optional<RequisitoCumplido> findById(Long id) {
        return requisitoCumplidoRepository.findById(id);
    }

    public void deleteById(Long id) {
        requisitoCumplidoRepository.deleteById(id);
    }
}