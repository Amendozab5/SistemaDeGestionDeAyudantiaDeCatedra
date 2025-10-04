package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.DatosEstudiantil;
import com.uteq.sgaac.model.DatosEstudiantilId;
import com.uteq.sgaac.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DatosEstudiantilRepository extends JpaRepository<DatosEstudiantil, DatosEstudiantilId> {

    List<DatosEstudiantil> findByEstudiante(Estudiante estudiante);

    Optional<DatosEstudiantil> findByEstudianteAndAsignatura(Estudiante estudiante, Asignatura asignatura);

    List<DatosEstudiantil> findByAsignatura(Asignatura asignatura);

    @Query("SELECT AVG(de.calificaciones) FROM DatosEstudiantil de WHERE de.asignatura.id = :asignaturaId AND de.periodo = :periodo")
    BigDecimal findAverageCalificacionByAsignaturaAndPeriodo(@Param("asignaturaId") Long asignaturaId, @Param("periodo") String periodo);

}