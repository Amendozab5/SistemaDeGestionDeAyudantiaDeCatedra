package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.*;
import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.model.Semestre;
import com.uteq.sgaac.repository.AsignaturaRepository;
import com.uteq.sgaac.repository.CarreraRepository;
import com.uteq.sgaac.repository.DocenteRepository;
import com.uteq.sgaac.repository.SemestreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignaturaService {

    @Autowired
    private AsignaturaRepository asignaturaRepository;
    @Autowired
    private CarreraRepository carreraRepository;
    @Autowired
    private SemestreRepository semestreRepository;
    @Autowired
    private DocenteRepository docenteRepository;

    public List<Asignatura> findAll() {
        return asignaturaRepository.findAll();
    }

    public List<AsignaturaSimpleDTO> findAllAsSimpleDTO() {
        return asignaturaRepository.findAll().stream()
                .map(asignatura -> new AsignaturaSimpleDTO(asignatura.getIdAsignatura(), asignatura.getNombre()))
                .collect(Collectors.toList());
    }

    public List<Asignatura> findByCarreraAndSemestre(Long idCarrera, int idSemestre) {
        return asignaturaRepository.findByCarreraAndSemestreIds(idCarrera, idSemestre);
    }

    @Transactional
    public AsignaturaDetailDTO update(Long id, AsignaturaUpdateDTO dto) {
        Asignatura asignaturaToUpdate = asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con id: " + id));

        asignaturaToUpdate.setCodigo(dto.getCodigo());
        asignaturaToUpdate.setNombre(dto.getNombre());

        if (dto.getId_carrera() != null) {
            Carrera carrera = carreraRepository.findById(dto.getId_carrera())
                    .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
            asignaturaToUpdate.setCarrera(carrera);
        }

        if (dto.getId_semestre() != null) {
            Semestre semestre = semestreRepository.findById(dto.getId_semestre())
                    .orElseThrow(() -> new RuntimeException("Semestre no encontrado"));
            asignaturaToUpdate.setSemestre(semestre);
        }

        // --- Lógica de asignación de Docente corregida ---

        // Si se proporciona un nuevo ID de docente
        if (dto.getId_docente() != null) {
            Docente nuevoDocente = docenteRepository.findById(dto.getId_docente())
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

            // Desvincular el docente actual (si existe y es diferente al nuevo)
            if (asignaturaToUpdate.getDocente() != null && !asignaturaToUpdate.getDocente().equals(nuevoDocente)) {
                Docente docenteAntiguo = asignaturaToUpdate.getDocente();
                docenteAntiguo.setAsignatura(null);
                docenteRepository.save(docenteAntiguo);
            }
            
            // Desvincular al nuevo docente de su antigua asignatura (si la tiene)
            if (nuevoDocente.getAsignatura() != null && !nuevoDocente.getAsignatura().equals(asignaturaToUpdate)) {
                Asignatura asignaturaAntiguaDelNuevoDocente = nuevoDocente.getAsignatura();
                asignaturaAntiguaDelNuevoDocente.setDocente(null);
                asignaturaRepository.save(asignaturaAntiguaDelNuevoDocente);
            }

            // Asignar el nuevo docente a esta asignatura
            asignaturaToUpdate.setDocente(nuevoDocente);
            nuevoDocente.setAsignatura(asignaturaToUpdate);
            docenteRepository.save(nuevoDocente);

        } else { // Si no se proporciona un ID de docente, se desasigna el actual
            if (asignaturaToUpdate.getDocente() != null) {
                Docente docenteActual = asignaturaToUpdate.getDocente();
                docenteActual.setAsignatura(null);
                docenteRepository.save(docenteActual);
                asignaturaToUpdate.setDocente(null);
            }
        }


        Asignatura savedAsignatura = asignaturaRepository.save(asignaturaToUpdate);
        return findDetailById(savedAsignatura.getIdAsignatura());
    }

    public AsignaturaDetailDTO findDetailById(Long id) {
        Asignatura asignatura = asignaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignatura no encontrada con id: " + id));

        AsignaturaDetailDTO dto = new AsignaturaDetailDTO();
        dto.setIdAsignatura(asignatura.getIdAsignatura());
        dto.setCodigo(asignatura.getCodigo());
        dto.setNombre(asignatura.getNombre());

        if (asignatura.getSemestre() != null) {
            dto.setSemestre(new SemestreDTO(asignatura.getSemestre().getIdSemestre(), asignatura.getSemestre().getDescripcion()));
        }

        if (asignatura.getCarrera() != null) {
            dto.setCarrera(new CarreraDTO(asignatura.getCarrera().getId(), asignatura.getCarrera().getCarrera()));
        }

        if (asignatura.getDocente() != null) {
            Docente docente = asignatura.getDocente();
            String nombreCompleto = docente.getUsuario().getNombres() + " " + docente.getUsuario().getApellidos();
            dto.setDocente(new DocenteDTO(docente.getId(), nombreCompleto));
        }

        return dto;
    }

    public boolean delete(Long id) {
        if (asignaturaRepository.existsById(id)) {
            asignaturaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
