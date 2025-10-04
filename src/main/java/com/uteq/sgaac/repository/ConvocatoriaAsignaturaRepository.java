package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConvocatoriaAsignaturaRepository extends JpaRepository<ConvocatoriaAsignatura, Long> {

    List<ConvocatoriaAsignatura> findByConvocatoria(Convocatoria convocatoria);

    boolean existsByConvocatoriaAndAsignatura(Convocatoria convocatoria, Asignatura asignatura);

    Optional<ConvocatoriaAsignatura> findByConvocatoriaAndAsignatura(Convocatoria convocatoria, Asignatura asignatura);

    void deleteByConvocatoria(Convocatoria convocatoria);

    List<ConvocatoriaAsignatura> findByAprobadoDecanoFalse();

    @Query("SELECT ca FROM ConvocatoriaAsignatura ca WHERE ca.aprobadoDecano = false AND ca.asignatura.carrera.facultad = :facultad")
    List<ConvocatoriaAsignatura> findPlazasPendientesByFacultad(@Param("facultad") Facultad facultad);

    @Query("SELECT ca FROM ConvocatoriaAsignatura ca WHERE ca.aprobadoDecano = true AND ca.asignatura.carrera.facultad = :facultad")
    List<ConvocatoriaAsignatura> findPlazasAprobadasByFacultad(@Param("facultad") Facultad facultad);

    @Query("SELECT ca FROM ConvocatoriaAsignatura ca " +
           "WHERE ca.aprobadoDecano = true " +
           "AND ca.convocatoria.estado = true " +
           "AND ca.asignatura.carrera = :carrera ")
    List<ConvocatoriaAsignatura> findPlazasActivasPorCarrera(
        @Param("carrera") Carrera carrera,
        @Param("fechaActual") LocalDate fechaActual
    );

}
