package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Postulacion;
import com.uteq.sgaac.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    Optional<Resultado> findByPostulacion(Postulacion postulacion);
    List<Resultado> findByEstadoFinal(String estadoFinal);
}
