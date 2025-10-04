package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Convocatoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {

    List<Convocatoria> findByEstado(boolean estado);

    @Query("SELECT DISTINCT c FROM Convocatoria c LEFT JOIN FETCH c.convocatoriaAsignaturas ca LEFT JOIN FETCH ca.asignatura")
    List<Convocatoria> findAllWithDetails();

    @Query("SELECT DISTINCT c FROM Convocatoria c JOIN FETCH c.convocatoriaAsignaturas ca WHERE c.estado = :estado AND ca.aprobadoDecano = true")
    List<Convocatoria> findListasParaPublicar(@Param("estado") boolean estado);
}