package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AyudanteCatedra;
import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AyudanteCatedraRepository extends JpaRepository<AyudanteCatedra, Long> {
    List<AyudanteCatedra> findByEstudiante(Estudiante estudiante);
    Optional<AyudanteCatedra> findByPostulacion(Postulacion postulacion);
}
