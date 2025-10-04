package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.DocenteDTO;
import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public List<DocenteDTO> findAllDocentesAsDTO() {
        List<Docente> docentes = docenteRepository.findAll();
        return docentes.stream().map(docente -> new DocenteDTO(
                docente.getId(),
                docente.getUsuario().getNombres() + " " + docente.getUsuario().getApellidos()
        )).collect(Collectors.toList());
    }

    public List<DocenteDTO> findDocentesByAsignatura(Long asignaturaId) {
        List<Docente> docentes = docenteRepository.findByAsignatura_IdAsignatura(asignaturaId);
        return docentes.stream().map(docente -> new DocenteDTO(
                docente.getId(),
                docente.getUsuario().getNombres() + " " + docente.getUsuario().getApellidos()
        )).collect(Collectors.toList());
    }
}
