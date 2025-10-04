package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.EvaluacionMeritos;
import com.uteq.sgaac.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluacionMeritosRepository extends JpaRepository<EvaluacionMeritos, Long> {
    List<EvaluacionMeritos> findByEstado(String estado);
    Optional<EvaluacionMeritos> findByPostulacion(Postulacion postulacion);
}
