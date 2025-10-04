package com.uteq.sgaac.repository;

//import com.uteq.sgaac.model.Asignatura;
import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.Postulacion;
import com.uteq.sgaac.model.PostulacionEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
//import java.util.Optional;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    long countByEstudianteAndEstadoPostulacionIn(Estudiante estudiante, List<PostulacionEstado> estados);

    List<Postulacion> findByEstadoPostulacionAndAsignatura_IdAsignaturaIn(PostulacionEstado estado, List<Long> asignaturaIds);

    List<Postulacion> findByEstudiante(Estudiante estudiante);

    @Query("SELECT p FROM Postulacion p " +
           "LEFT JOIN FETCH p.estudiante e " +
           "LEFT JOIN FETCH e.usuario u " +
           "LEFT JOIN FETCH p.asignatura a " +
           "LEFT JOIN FETCH p.convocatoria c " +
           "WHERE p.estadoPostulacion = :estado AND a.idAsignatura IN :asignaturaIds")
    List<Postulacion> findPostulacionesPendientesByAsignaturas(@Param("estado") PostulacionEstado estado, @Param("asignaturaIds") List<Long> asignaturaIds);

    @Query("SELECT p FROM Postulacion p " +
           "LEFT JOIN FETCH p.estudiante e " +
           "LEFT JOIN FETCH e.usuario u " +
           "LEFT JOIN FETCH e.carrera " +
           "LEFT JOIN FETCH p.asignatura a " +
           "LEFT JOIN FETCH p.convocatoria c " +
           "WHERE p.estadoPostulacion = :estado")
    List<Postulacion> findByEstadoPostulacionWithDetails(@Param("estado") PostulacionEstado estado);


    @Query("SELECT p FROM Postulacion p " +
           "INNER JOIN FETCH p.resultado r " +
           "INNER JOIN FETCH p.estudiante e " +
           "INNER JOIN FETCH e.usuario u " +
           "INNER JOIN FETCH e.carrera c " +
           "INNER JOIN FETCH p.asignatura a " +
           "WHERE r.estadoFinal = 'GANADOR' AND NOT EXISTS (SELECT 1 FROM AyudanteCatedra ac WHERE ac.postulacion = p)")
    List<Postulacion> findGanadoresNoConvertidos();

    @Query("SELECT p FROM Postulacion p LEFT JOIN FETCH p.documentos d LEFT JOIN FETCH d.requisito")
    List<Postulacion> findAllWithDocumentos();

}
