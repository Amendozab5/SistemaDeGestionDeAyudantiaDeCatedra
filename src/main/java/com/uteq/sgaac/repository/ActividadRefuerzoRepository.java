package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.ActividadRefuerzo;
import com.uteq.sgaac.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRefuerzoRepository extends JpaRepository<ActividadRefuerzo, Long> {
    List<ActividadRefuerzo> findByAyudante(Estudiante ayudante);
}
