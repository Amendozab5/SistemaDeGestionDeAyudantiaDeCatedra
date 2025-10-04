package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.Docente;
import com.uteq.sgaac.model.PruebaOposicion;
import com.uteq.sgaac.model.PruebaOposicionEval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PruebaOposicionEvalRepository extends JpaRepository<PruebaOposicionEval, Long> {
    Optional<PruebaOposicionEval> findByOposicionAndDocente(PruebaOposicion oposicion, Docente docente);

    List<PruebaOposicionEval> findByOposicion(PruebaOposicion oposicion);

    void deleteByOposicion(PruebaOposicion oposicion);
}
