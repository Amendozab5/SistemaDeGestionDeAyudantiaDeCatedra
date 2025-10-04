package com.uteq.sgaac.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Docente;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    // Consulta para obtener los IDs de semestres únicos y ordenados
    @Query("SELECT DISTINCT a.semestre.idSemestre FROM Asignatura a WHERE a.semestre.idSemestre IS NOT NULL ORDER BY a.semestre.idSemestre ASC")
    List<Integer> findDistinctSemestres();

    // Consulta para encontrar asignaturas por el ID del semestre (usado por AsignaturaApiController)
    List<Asignatura> findBySemestre_IdSemestre(Integer idSemestre);

    // Consulta personalizada para obtener asignaturas según carrera y semestre
    @Query("SELECT a FROM Asignatura a WHERE a.carrera.id = :carreraId AND a.semestre.idSemestre = :semestreId")
    List<Asignatura> findByCarreraAndSemestreIds(@Param("carreraId") Long carreraId, @Param("semestreId") int semestreId);

    @Query("SELECT a FROM Asignatura a WHERE a.carrera.id = :carreraId AND a.semestre.idSemestre < :semestreId")
    List<Asignatura> findByCarreraIdAndSemestreIdLessThan(@Param("carreraId") Long carreraId, @Param("semestreId") Long semestreId);

    List<Asignatura> findByDocente(Docente docente);
}
