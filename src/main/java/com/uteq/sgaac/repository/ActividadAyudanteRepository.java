package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.ActividadAyudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadAyudanteRepository extends JpaRepository<ActividadAyudante, Long> {
}
