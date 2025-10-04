package com.uteq.sgaac.repository;

import com.uteq.sgaac.model.AudActividadAyudante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AudActividadAyudanteRepository extends JpaRepository<AudActividadAyudante, Long>, JpaSpecificationExecutor<AudActividadAyudante> {
}
