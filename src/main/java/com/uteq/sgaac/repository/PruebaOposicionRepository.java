package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.Postulacion;
import com.uteq.sgaac.model.PruebaOposicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PruebaOposicionRepository extends JpaRepository<PruebaOposicion, Long> {

    /**
     * Busca todas las pruebas de oposición asociadas a un estudiante a través de sus postulaciones.
     * Las ordena por fecha de oposición descendente para mostrar las más recientes primero.
     * @param estudiante El estudiante para el cual se buscan las pruebas.
     * @return Una lista de pruebas de oposición.
     */
    List<PruebaOposicion> findByPostulacion_EstudianteOrderByFechaOposicionDesc(Estudiante estudiante);

    List<PruebaOposicion> findByEstado(String estado);

    @Query("SELECT p FROM PruebaOposicion p JOIN FETCH p.postulacion pos JOIN FETCH pos.estudiante e JOIN FETCH e.usuario JOIN FETCH pos.asignatura WHERE p.estado = :estado")
    List<PruebaOposicion> findByEstadoWithDetails(@Param("estado") String estado);

    @Query("SELECT p FROM PruebaOposicion p JOIN FETCH p.postulacion pos JOIN FETCH pos.estudiante e JOIN FETCH e.usuario JOIN FETCH pos.asignatura WHERE p.estado IN :statuses")
    List<PruebaOposicion> findByEstadoInWithDetails(@Param("statuses") List<String> statuses);

    @Query("SELECT p FROM PruebaOposicion p WHERE p.postulacion.id = :idPostulacion")
    Optional<PruebaOposicion> findByPostulacionId(@Param("idPostulacion") Long idPostulacion);

    Optional<PruebaOposicion> findByPostulacion(Postulacion postulacion);

}
