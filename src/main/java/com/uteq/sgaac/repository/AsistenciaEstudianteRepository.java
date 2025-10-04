package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AsistenciaEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaEstudianteRepository extends JpaRepository<AsistenciaEstudiante, Long> {
}
